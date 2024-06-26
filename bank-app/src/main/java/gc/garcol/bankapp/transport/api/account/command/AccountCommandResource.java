package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.apputil.observation.TracingUtil;
import gc.garcol.bankapp.service.AccountCommandDispatcher;
import gc.garcol.bankapp.service.SimpleAccountRequestReplyFuture;
import gc.garcol.bankapp.transport.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountCommandResource {

    private final AccountCommandDispatcher accountCommandDispatcher;
    private final SimpleAccountRequestReplyFuture accountRequestReplyFuture;

    @PostMapping("/create")
    public CompletableFuture<ResponseWrapper> createAccount() {
        TracingUtil.startNewSpan("create");
        var createAccountCommand = new CreateAccountCommand();
        log.debug("Creating account with : {}", createAccountCommand);
        return accountRequestReplyFuture.request(createAccountCommand.getCorrelationId(),
                () -> accountCommandDispatcher.createAccount(createAccountCommand)
        );
    }

    @PostMapping("/deposit")
    public CompletableFuture<ResponseWrapper> deposit(@RequestBody DepositAccountCommand depositCommand) {
        TracingUtil.startNewSpan("deposit");
        log.debug("Depositing with: {}", depositCommand);
        return accountRequestReplyFuture.request(depositCommand.getCorrelationId(),
                () -> accountCommandDispatcher.deposit(depositCommand)
        );
    }

    @PostMapping("/withdraw")
    public CompletableFuture<ResponseWrapper> withdraw(@RequestBody WithdrawAccountCommand withdrawCommand) {
        TracingUtil.startNewSpan("withdraw");
        log.debug("Withdrawing with correlationId: {}", withdrawCommand.getCorrelationId());
        return accountRequestReplyFuture.request(withdrawCommand.getCorrelationId(),
                () -> accountCommandDispatcher.withdraw(withdrawCommand)
        );
    }

    @PostMapping("/transfer")
    public CompletableFuture<ResponseWrapper> transfer(@RequestBody TransferBalanceCommand transferCommand) {
        TracingUtil.startNewSpan("transfer");
        log.debug("Transferring with: {}", transferCommand);
        return accountRequestReplyFuture.request(transferCommand.getCorrelationId(),
                () -> accountCommandDispatcher.transfer(transferCommand)
        );
    }
}
