package gc.garcol.bankapp.app;

import gc.garcol.bankapp.service.AccountClusterEgressListener;
import gc.garcol.bankapp.service.AccountCommandDispatcher;
import gc.garcol.bankapp.service.AccountCommandDispatcherImpl;
import gc.garcol.bankapp.service.AccountCommandHandler;
import gc.garcol.bankapp.service.AccountCommandHandlerImpl;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

@Configuration
public class AccountConfiguration {

    @Bean
    public IdleStrategy commandIdleStrategy() {
        return new SleepingMillisIdleStrategy();
    }

    @Bean
    public OneToOneRingBuffer commandBuffer() {
        final var adminClusterBuffer =
            new UnsafeBuffer(BufferUtil.allocateDirectAligned((1 << 13) + TRAILER_LENGTH, 8));
        return new OneToOneRingBuffer(adminClusterBuffer);
    }

    @Bean
    public AccountCommandDispatcher accountCommandDispatcher(OneToOneRingBuffer commandBuffer) {
        var accountCommandDispatcher = new AccountCommandDispatcherImpl();
        accountCommandDispatcher.setCommandBuffer(commandBuffer);
        return accountCommandDispatcher;
    }

    @Bean
    public AccountCommandHandler accountCommandHandler(
        final OneToOneRingBuffer commandBuffer,
        final AccountClusterEgressListener egressListener
    ) {
        var accountCommandHandler = new AccountCommandHandlerImpl();
        accountCommandHandler.setCommandBuffer(commandBuffer);
        accountCommandHandler.setEgressListener(egressListener);
        return accountCommandHandler;
    }
}
