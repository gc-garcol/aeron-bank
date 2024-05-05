package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.SbeCommandDispatcherImpl;
import gc.garcol.common.core.CommandHandlerMethod;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.protocol.AddAccountCommandDecoder;
import gc.garcol.protocol.DepositAccountCommandDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
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

    private final MessageHeaderDecoder messageHeaderDecoder;
    private final Accounts accounts;

    @Bean
    public SbeCommandDispatcher accountSbeCommandDispatcher() {
        return new SbeCommandDispatcherImpl(messageHeaderDecoder);
    }

    @Bean
    public CommandHandlerMethod addAccountCommandHandler(
        SbeCommandDispatcher accountSbeCommandDispatcher,
        AddAccountCommandDecoder addAccountCommandDecoder
    ) {
        CommandHandlerMethod handler = (buffer, offset) ->
            addAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accountSbeCommandDispatcher.registerHandler(AddAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }

    @Bean
    public CommandHandlerMethod depositAccountCommandHandler(
        SbeCommandDispatcher accountSbeCommandDispatcher,
        DepositAccountCommandDecoder depositAccountCommandDecoder
    ) {
        CommandHandlerMethod handler = (buffer, offset) -> {
            depositAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
            accounts.openAccount(depositAccountCommandDecoder.accountId());
        };
        accountSbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }

    @Bean
    public CommandHandlerMethod withdrawAccountCommandHandler(
        SbeCommandDispatcher accountSbeCommandDispatcher,
        DepositAccountCommandDecoder depositAccountCommandDecoder
    ) {
        CommandHandlerMethod handler = (buffer, offset) -> {
            depositAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
            accounts.deposit(depositAccountCommandDecoder.accountId(), depositAccountCommandDecoder.amount());
        };
        accountSbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }
}
