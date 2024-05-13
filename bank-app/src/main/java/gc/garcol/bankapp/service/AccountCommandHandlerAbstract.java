package gc.garcol.bankapp.service;

import gc.garcol.protocol.ConnectClusterDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
import gc.garcol.protocol.MessageHeaderEncoder;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;

public abstract class AccountCommandHandlerAbstract implements Agent, MessageHandler {

    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();

    protected final ConnectClusterDecoder connectClusterDecoder = new ConnectClusterDecoder();

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
