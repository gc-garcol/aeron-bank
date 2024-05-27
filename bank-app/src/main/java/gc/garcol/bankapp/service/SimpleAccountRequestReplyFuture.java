package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.BaseError;
import gc.garcol.bankapp.transport.BaseResponse;
import gc.garcol.bankapp.transport.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class SimpleAccountRequestReplyFuture {

    private final Map<String, CompletableFuture<ResponseWrapper>> futures = new ConcurrentHashMap<>();
    private final ScheduledExecutorService timeoutScheduler = Executors.newSingleThreadScheduledExecutor();

    public CompletableFuture<ResponseWrapper> request(String correlationId, Runnable runnable) {
        var completableFuture = new CompletableFuture<ResponseWrapper>();
        futures.put(correlationId, completableFuture);
        runnable.run();
        timeoutScheduler.schedule(() -> {
            futures.remove(correlationId);
            var timeout = new ResponseWrapper();
            timeout.setStatus(HttpStatus.REQUEST_TIMEOUT.value());
            timeout.setError(new BaseError(String.format("[%s] Creating account reached timeout", correlationId)));
            completableFuture.complete(timeout);
        }, 10, TimeUnit.SECONDS);
        return completableFuture;
    }

    public void replySuccess(String correlationId, BaseResponse responseData) {
        var future = futures.remove(correlationId);
        if (future == null) return;
        var response = new ResponseWrapper();
        response.setData(responseData);
        response.setStatus(HttpStatus.OK.value());
        future.complete(response);
    }

    public void replyFail(String correlationId, BaseError error) {
        var future = futures.remove(correlationId);
        if (future == null) return;
        var response = new ResponseWrapper();
        response.setError(error);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        future.complete(response);
    }
}
