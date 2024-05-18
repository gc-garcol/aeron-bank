package gc.garcol.bankcluster.infra;

import gc.garcol.bankcluster.domain.account.Accounts;
import lombok.RequiredArgsConstructor;
import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class AccountCommandHandlerImpl extends AccountCommandHandlerAbstract {
    private final Accounts accounts;

    @Override
    public void createAccountCommandHandler(DirectBuffer buffer, int offset) {
        createAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accounts.openAccount(createAccountCommandDecoder.correlationId());
    }

    @Override
    public void depositAccountCommandHandler(DirectBuffer buffer, int offset) {
        depositAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accounts.withdraw(
            depositAccountCommandDecoder.correlationId(),
            depositAccountCommandDecoder.accountId(),
            depositAccountCommandDecoder.amount()
        );
    }

    @Override
    public void withdrawAccountCommandHandler(DirectBuffer buffer, int offset) {
        withdrawAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accounts.deposit(
            withdrawAccountCommandDecoder.correlationId(),
            withdrawAccountCommandDecoder.accountId(),
            withdrawAccountCommandDecoder.amount()
        );
    }

    @Override
    public void transferAccountCommandHandler(DirectBuffer buffer, int offset) {
        transferAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accounts.transfer(
            transferAccountCommandDecoder.correlationId(),
            transferAccountCommandDecoder.fromAccountId(),
            transferAccountCommandDecoder.toAccountId(),
            transferAccountCommandDecoder.amount()
        );
    }
}
