package gc.garcol.bankapp.service;

import gc.garcol.protocol.*;
import lombok.Getter;
import org.agrona.MutableDirectBuffer;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.concurrent.Agent;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

public abstract class AccountCommandHandlerAbstract
    implements Agent, MessageHandler, AccountCommandHandler, SystemCommandHandler, CommandBufferChannel {

    private static final CommandBufferHandler DEFAULT_NOT_FOUND_HANDLER = (buffer, offset) -> {
        throw new IllegalArgumentException("Unknown message type");
    };

    @Getter
    protected final OneToOneRingBuffer commandBuffer;
    protected final Int2ObjectHashMap<CommandBufferHandler> handlers = new Int2ObjectHashMap<>();

    protected AccountCommandHandlerAbstract(OneToOneRingBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
        handlers.put(ConnectClusterDecoder.TEMPLATE_ID, this::processConnectCluster);
        handlers.put(DisconnectClusterDecoder.TEMPLATE_ID, this::processDisconnectCluster);
        handlers.put(CreateAccountCommandBufferDecoder.TEMPLATE_ID, this::processCreateAccountCommand);
        handlers.put(DepositAccountCommandBufferDecoder.TEMPLATE_ID, this::processDepositAccountCommand);
        handlers.put(WithdrawAccountCommandBufferDecoder.TEMPLATE_ID, this::processWithdrawAccountCommand);
        handlers.put(TransferAccountCommandBufferDecoder.TEMPLATE_ID, this::processTransferBalanceCommand);
    }

    protected final ConnectClusterDecoder connectClusterDecoder = new ConnectClusterDecoder();
    protected final DisconnectClusterDecoder disconnectClusterDecoder = new DisconnectClusterDecoder();

    // messages from commandBuffer, sent by client
    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    protected final CreateAccountCommandBufferDecoder createAccountCommandBufferDecoder = new CreateAccountCommandBufferDecoder();
    protected final DepositAccountCommandBufferDecoder depositAccountCommandBufferDecoder = new DepositAccountCommandBufferDecoder();
    protected final WithdrawAccountCommandBufferDecoder withdrawAccountCommandBufferDecoder = new WithdrawAccountCommandBufferDecoder();
    protected final TransferAccountCommandBufferDecoder transferAccountCommandBufferDecoder = new TransferAccountCommandBufferDecoder();

    // messages to commandBuffer, sent by system
    protected final ConnectClusterDecoder connectClusterCommandBufferDecoder = new ConnectClusterDecoder();
    protected final DisconnectClusterDecoder disconnectClusterCommandBufferDecoder = new DisconnectClusterDecoder();

    // messages would be sent to cluster
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final CreateAccountCommandEncoder createAccountCommandEncoder = new CreateAccountCommandEncoder();
    protected final DepositAccountCommandEncoder depositAccountCommandEncoder = new DepositAccountCommandEncoder();
    protected final WithdrawAccountCommandEncoder withdrawAccountCommandEncoder = new WithdrawAccountCommandEncoder();
    protected final TransferAccountCommandEncoder transferAccountCommandEncoder = new TransferAccountCommandEncoder();

    @Override
    public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int offset, int length) {
        messageHeaderDecoder.wrap(buffer, offset);
        handlers.getOrDefault(messageHeaderDecoder.templateId() ,DEFAULT_NOT_FOUND_HANDLER).process(buffer, offset);
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
