package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.AccountCommandHandler;
import gc.garcol.cluster.infra.AccountCommandHandlerImpl;
import gc.garcol.cluster.infra.SbeCommandDispatcherImpl;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.protocol.*;
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

    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    @Bean
    public SbeCommandDispatcher accountSbeCommandDispatcher() {
        return new SbeCommandDispatcherImpl(messageHeaderDecoder);
    }

    @Bean
    public AccountCommandHandler accountCommandHandler(SbeCommandDispatcher sbeCommandDispatcher) {
        var accountHandler = new AccountCommandHandlerImpl(accounts);
        sbeCommandDispatcher.registerHandler(AddAccountCommandDecoder.TEMPLATE_ID, accountHandler::addAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, accountHandler::depositAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(WithdrawAccountCommandDecoder.TEMPLATE_ID, accountHandler::withdrawAccountCommandHandler);
        sbeCommandDispatcher.registerHandler(TransferAccountCommandDecoder.TEMPLATE_ID, accountHandler::transferAccountCommandHandler);
        return accountHandler;
    }

}
