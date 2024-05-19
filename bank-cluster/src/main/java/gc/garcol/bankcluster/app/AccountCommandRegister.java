package gc.garcol.bankcluster.app;

import gc.garcol.bankcluster.domain.account.Accounts;
import gc.garcol.bankcluster.infra.AccountCommandHandler;
import gc.garcol.bankcluster.infra.AccountCommandHandlerImpl;
import gc.garcol.bankcluster.infra.SbeCommandDispatcherImpl;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.protocol.CreateAccountCommandDecoder;
import gc.garcol.protocol.DepositAccountCommandDecoder;
import gc.garcol.protocol.TransferAccountCommandDecoder;
import gc.garcol.protocol.WithdrawAccountCommandDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thaivc
 * @since 2024
 */
@Configuration
@RequiredArgsConstructor
public class AccountCommandRegister {
    private final Accounts accounts;

    @Bean
    public SbeCommandDispatcher accountSbeCommandDispatcher() {
        return new SbeCommandDispatcherImpl();
    }

    @Bean
    public AccountCommandHandler accountCommandHandler(SbeCommandDispatcher sbeCommandDispatcher) {
        var accountHandler = new AccountCommandHandlerImpl(accounts);
        sbeCommandDispatcher.registerHandler(CreateAccountCommandDecoder.TEMPLATE_ID, accountHandler::createAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, accountHandler::depositAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(WithdrawAccountCommandDecoder.TEMPLATE_ID, accountHandler::withdrawAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(TransferAccountCommandDecoder.TEMPLATE_ID, accountHandler::transferAccountCommandHandler);
        return accountHandler;
    }

}
