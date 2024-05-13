package gc.garcol.bankapp.service;

import gc.garcol.protocol.*;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;

public abstract class AccountCommandHandlerAbstract implements Agent, MessageHandler {


    protected final ConnectClusterDecoder connectClusterDecoder = new ConnectClusterDecoder();
    protected final DisconnectClusterDecoder disconnectClusterDecoder = new DisconnectClusterDecoder();

    // messages from commandBuffer
    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    protected final CreateAccountCommandBufferDecoder createAccountCommandBufferDecoder = new CreateAccountCommandBufferDecoder();
    protected final DepositAccountCommandBufferDecoder depositAccountCommandBufferDecoder = new DepositAccountCommandBufferDecoder();
    protected final WithdrawAccountCommandBufferDecoder withdrawAccountCommandBufferDecoder = new WithdrawAccountCommandBufferDecoder();
    protected final TransferAccountCommandBufferDecoder transferAccountCommandBufferDecoder = new TransferAccountCommandBufferDecoder();

    // messages would be sent to cluster
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final CreateAccountCommandEncoder createAccountCommandEncoder = new CreateAccountCommandEncoder();
    protected final DepositAccountCommandEncoder depositAccountCommandEncoder = new DepositAccountCommandEncoder();
    protected final WithdrawAccountCommandEncoder withdrawAccountCommandEncoder = new WithdrawAccountCommandEncoder();
    protected final TransferAccountCommandEncoder transferAccountCommandEncoder = new TransferAccountCommandEncoder();

    @Override
    public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int index, int length) {

    }

    @Override
    public int doWork() throws Exception {
        return 0;
    }

    @Override
    public String roleName() {
        return null;
    }
}
