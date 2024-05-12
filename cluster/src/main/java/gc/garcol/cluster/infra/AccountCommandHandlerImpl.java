package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.protocol.*;
import lombok.RequiredArgsConstructor;
import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class AccountCommandHandlerImpl implements AccountCommandHandler {
    private final Accounts accounts;

    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    private final WithdrawAccountCommandDecoder withdrawAccountCommandDecoder = new WithdrawAccountCommandDecoder();
    private final DepositAccountCommandDecoder depositAccountCommandDecoder = new DepositAccountCommandDecoder();
    private final AddAccountCommandDecoder addAccountCommandDecoder = new AddAccountCommandDecoder();
    private final TransferAccountCommandDecoder transferAccountCommandDecoder = new TransferAccountCommandDecoder();

    @Override
    public void addAccountCommandHandler(DirectBuffer buffer, int offset) {
        addAccountCommandDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        accounts.openAccount(addAccountCommandDecoder.correlationId(), addAccountCommandDecoder.accountId());
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
