package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.SbeCommandDispatcherImpl;
import gc.garcol.common.core.CommandHandlerMethod;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.protocol.AddAccountCommandDecoder;
import gc.garcol.protocol.DepositAccountCommandDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
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
    // Inject
    private final Accounts accounts;

    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private final WithdrawAccountCommandDecoder withdrawAccountCommandDecoder = new WithdrawAccountCommandDecoder();
    private final DepositAccountCommandDecoder depositAccountCommandDecoder = new DepositAccountCommandDecoder();
    private final AddAccountCommandDecoder addAccountCommandDecoder = new AddAccountCommandDecoder();

    @Bean
    public SbeCommandDispatcher accountSbeCommandDispatcher() {
        return new SbeCommandDispatcherImpl(messageHeaderDecoder);
    }

    @Bean
    public CommandHandlerMethod addAccountCommandHandler(SbeCommandDispatcher accountSbeCommandDispatcher) {
        CommandHandlerMethod handler = (buffer, offset) ->
            addAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accountSbeCommandDispatcher.registerHandler(AddAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }

    @Bean
    public CommandHandlerMethod depositAccountCommandHandler(SbeCommandDispatcher accountSbeCommandDispatcher) {
        CommandHandlerMethod handler = (buffer, offset) -> {
            depositAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
            accounts.openAccount(depositAccountCommandDecoder.accountId());
        };
        accountSbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }

    @Bean
    public CommandHandlerMethod withdrawAccountCommandHandler(SbeCommandDispatcher accountSbeCommandDispatcher) {
        CommandHandlerMethod handler = (buffer, offset) -> {
            withdrawAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
            accounts.deposit(withdrawAccountCommandDecoder.accountId(), withdrawAccountCommandDecoder.amount());
        };
        accountSbeCommandDispatcher.registerHandler(DepositAccountCommandDecoder.TEMPLATE_ID, handler);
        return handler;
    }
}
