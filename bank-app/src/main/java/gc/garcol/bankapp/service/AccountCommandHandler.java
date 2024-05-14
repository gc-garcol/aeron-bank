package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountCommandHandler extends Agent, MessageHandler {
    void sendToClusterCreateAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void sendToClusterDepositAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void sendToClusterWithdrawAccountCommand(final MutableDirectBuffer buffer, final int offset);

    void sendToClusterTransferBalanceCommand(final MutableDirectBuffer buffer, final int offset);
}
