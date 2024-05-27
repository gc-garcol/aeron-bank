package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.service.AccountCommandDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/account-async")
@RequiredArgsConstructor
public class AccountCommandAsyncResource {

    private final AccountCommandDispatcher accountCommandDispatcher;

    @PostMapping("/create")
    public ResponseEntity<String> createAccountAsync(@RequestBody CreateAccountCommand createAccountCommand) {
        log.info("Creating async account with correlationId: {}", createAccountCommand.getCorrelationId());
        accountCommandDispatcher.createAccount(createAccountCommand);
        return ResponseEntity.ok(createAccountCommand.getCorrelationId());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositAsync(@RequestBody DepositAccountCommand depositAccountCommand) {
        log.info("Depositing async {} to account {} with correlationId: {}", depositAccountCommand.getAmount(), depositAccountCommand.getAccountId(), depositAccountCommand.getCorrelationId());
        accountCommandDispatcher.deposit(depositAccountCommand);
        return ResponseEntity.ok(depositAccountCommand.getCorrelationId());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawAsync(@RequestBody WithdrawAccountCommand withdrawAccountCommand) {
        log.info("Withdrawing async {} from account {} with correlationId: {}", withdrawAccountCommand.getAmount(), withdrawAccountCommand.getAccountId(), withdrawAccountCommand.getCorrelationId());
        accountCommandDispatcher.withdraw(withdrawAccountCommand);
        return ResponseEntity.ok(withdrawAccountCommand.getCorrelationId());
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferAsync(@RequestBody TransferBalanceCommand transferAccountCommand) {
        log.info("Transferring async {} from account {} to account {} with correlationId: {}", transferAccountCommand.getAmount(), transferAccountCommand.getFromAccountId(), transferAccountCommand.getToAccountId(), transferAccountCommand.getCorrelationId());
        accountCommandDispatcher.transfer(transferAccountCommand);
        return ResponseEntity.ok(transferAccountCommand.getCorrelationId());
    }
}
