package gc.garcol.bankapp.transport.api.account;

import gc.garcol.bankapp.service.AccountCommandDispatcherImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountResource {

    private final AccountCommandDispatcherImpl accountCommandInboundService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestBody CreateAccountCommand createAccountCommand) {
        log.info("Creating account with correlationId: {}", createAccountCommand);
        accountCommandInboundService.createAccount(createAccountCommand);
        return ResponseEntity.ok(createAccountCommand.getCorrelationId());
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositAccountCommand depositAccountCommand) {
        log.info("Depositing {} to account {} with correlationId: {}", depositAccountCommand.getAmount(), depositAccountCommand.getAccountId(), depositAccountCommand.getCorrelationId());
        accountCommandInboundService.deposit(depositAccountCommand);
        return ResponseEntity.ok(depositAccountCommand.getCorrelationId());
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawAccountCommand withdrawAccountCommand) {
        log.info("Withdrawing {} from account {} with correlationId: {}", withdrawAccountCommand.getAmount(), withdrawAccountCommand.getAccountId(), withdrawAccountCommand.getCorrelationId());
        accountCommandInboundService.withdraw(withdrawAccountCommand);
        return ResponseEntity.ok(withdrawAccountCommand.getCorrelationId());
    }
    @PostMapping
    public ResponseEntity<String> transfer(@RequestBody TransferBalanceCommand transferAccountCommand) {
        log.info("Transferring {} from account {} to account {} with correlationId: {}", transferAccountCommand.getAmount(), transferAccountCommand.getFromAccountId(), transferAccountCommand.getToAccountId(), transferAccountCommand.getCorrelationId());
        accountCommandInboundService.transfer(transferAccountCommand);
        return ResponseEntity.ok(transferAccountCommand.getCorrelationId());
    }
}
