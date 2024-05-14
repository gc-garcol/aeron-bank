package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountCommandHandler {
    void processCreateAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void processDepositAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void processWithdrawAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void processTransferBalanceCommand(final MutableDirectBuffer buffer, final int offset);
}
