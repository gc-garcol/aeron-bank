package gc.garcol.bankapp.service;

import org.agrona.DirectBuffer;
import org.springframework.stereotype.Component;

@Component
public class AccountClusterEgressListenerImpl extends AccountClusterEgressListenerAbstract {

    @Override
    public void processCreateAccount(DirectBuffer buffer, int offset) {

    }

    @Override
    public void processWithdrawAccount(DirectBuffer buffer, int offset) {

    }

    @Override
    public void processDepositAccount(DirectBuffer buffer, int offset) {

    }

    @Override
    public void processTransferAccount(DirectBuffer buffer, int offset) {

    }
}
