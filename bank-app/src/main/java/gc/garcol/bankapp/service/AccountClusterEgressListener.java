package gc.garcol.bankapp.service;

import io.aeron.cluster.client.EgressListener;
import org.agrona.DirectBuffer;

public interface AccountClusterEgressListener extends EgressListener {
    void processCreateAccount(DirectBuffer buffer, int offset);

    void processWithdrawAccount(DirectBuffer buffer, int offset);

    void processDepositAccount(DirectBuffer buffer, int offset);

    void processTransferAccount(DirectBuffer buffer, int offset);
}
