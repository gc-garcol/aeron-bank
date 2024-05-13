package gc.garcol.bankapp.transport.api.account;

import gc.garcol.bankapp.service.AccountCommandInboundService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountResource {

    private final AccountCommandInboundService accountCommandInboundService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount() {
        var correlationId = UUID.randomUUID().toString();
        var generatedAccountId = UUID.randomUUID().toString();
        log.info("Creating account with correlationId: {}", correlationId);
        accountCommandInboundService.addAccount(new CreateAccountCommand(correlationId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit() {
        var correlationId = UUID.randomUUID().toString();
        var accountId = UUID.randomUUID().toString();
        log.info("Depositing {} to account {} with correlationId: {}", amount, accountId, correlationId);
        accountCommandInboundService.deposit(new DepositAccountCommand(correlationId, accountId, amount));
        return ResponseEntity.ok().build();
    }


}
