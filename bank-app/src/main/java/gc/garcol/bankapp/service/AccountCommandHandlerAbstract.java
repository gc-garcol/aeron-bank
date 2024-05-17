package gc.garcol.bankapp.service;

import static gc.garcol.bankapp.service.CommandBufferHandler.DEFAULT_NOT_FOUND_HANDLER;

import gc.garcol.protocol.ConnectClusterDecoder;
import gc.garcol.protocol.CreateAccountCommandBufferDecoder;
import gc.garcol.protocol.CreateAccountCommandEncoder;
import gc.garcol.protocol.DepositAccountCommandBufferDecoder;
import gc.garcol.protocol.DepositAccountCommandEncoder;
import gc.garcol.protocol.DisconnectClusterDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
import gc.garcol.protocol.MessageHeaderEncoder;
import gc.garcol.protocol.TransferAccountCommandBufferDecoder;
import gc.garcol.protocol.TransferAccountCommandEncoder;
import gc.garcol.protocol.WithdrawAccountCommandBufferDecoder;
import gc.garcol.protocol.WithdrawAccountCommandEncoder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.agrona.MutableDirectBuffer;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

@Slf4j
public abstract class AccountCommandHandlerAbstract extends SystemCommandHandlerAbstract
    implements AccountCommandHandler, CommandBufferChannel {

    @Setter
    protected OneToOneRingBuffer commandBuffer;

    protected final Int2ObjectHashMap<CommandBufferHandler> handlers = new Int2ObjectHashMap<>();

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

        log.info("Received message with templateId={}", messageHeaderDecoder.templateId());
        handlers.getOrDefault(
            messageHeaderDecoder.templateId(),
            DEFAULT_NOT_FOUND_HANDLER
        ).process(buffer, offset);
    }

    @Override
    public int doWork() throws Exception {
        //poll inbound to this agent messages (from the REPL)
        commandBuffer.read(this);
        return 0;
    }

    @Override
    public String roleName() {
        return "account-agent";
    }
}
