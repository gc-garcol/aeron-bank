package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.springframework.stereotype.Service;

/**
 * @author thaivc
 * @since 2024
 */
@Service
public class AccountCommandHandlerImpl extends AccountCommandHandlerAbstract {

    protected AccountCommandHandlerImpl(OneToOneRingBuffer commandBuffer) {
        super(commandBuffer);
    }

    @Override
    public void processCreateAccountCommand(MutableDirectBuffer buffer, int offset) {
    }

    @Override
    public void processDepositAccountCommand(MutableDirectBuffer buffer, int offset) {

    }

    @Override
    public void processWithdrawAccountCommand(MutableDirectBuffer buffer, int offset) {

    }

    @Override
    public void processTransferBalanceCommand(MutableDirectBuffer buffer, int offset) {

    }

    @Override
    public void processConnectCluster(MutableDirectBuffer buffer, int offset) {

    }

    @Override
    public void processDisconnectCluster(MutableDirectBuffer buffer, int offset) {

    }
}
