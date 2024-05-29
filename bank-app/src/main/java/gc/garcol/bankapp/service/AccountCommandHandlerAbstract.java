package gc.garcol.bankapp.service;

import gc.garcol.bankapp.service.constants.ConnectionState;
import gc.garcol.protocol.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.collections.Int2ObjectHashMap;
import org.agrona.concurrent.SystemEpochClock;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

import static gc.garcol.bankapp.service.CommandBufferHandler.DEFAULT_NOT_FOUND_HANDLER;

@Slf4j
public abstract class AccountCommandHandlerAbstract extends SystemCommandHandlerAbstract
    implements AccountCommandHandler, CommandBufferChannel {

    private long lastHeartbeatTime = Long.MIN_VALUE;

    @Setter
    protected OneToOneRingBuffer commandBuffer;


    protected final MutableDirectBuffer sendBuffer = new ExpandableDirectByteBuffer(1 << 10);
    protected final Int2ObjectHashMap<CommandBufferHandler> handlers = new Int2ObjectHashMap<>();

    // messages from commandBuffer, sent by client
    protected final CreateAccountCommandBufferDecoder createAccountCommandBufferDecoder = new CreateAccountCommandBufferDecoder();
    protected final DepositAccountCommandBufferDecoder depositAccountCommandBufferDecoder = new DepositAccountCommandBufferDecoder();
    protected final WithdrawAccountCommandBufferDecoder withdrawAccountCommandBufferDecoder = new WithdrawAccountCommandBufferDecoder();
    protected final TransferAccountCommandBufferDecoder transferAccountCommandBufferDecoder = new TransferAccountCommandBufferDecoder();

    // messages would be sent to cluster
    protected final CreateAccountCommandEncoder createAccountCommandEncoder = new CreateAccountCommandEncoder();
    protected final DepositAccountCommandEncoder depositAccountCommandEncoder = new DepositAccountCommandEncoder();
    protected final WithdrawAccountCommandEncoder withdrawAccountCommandEncoder = new WithdrawAccountCommandEncoder();
    protected final TransferAccountCommandEncoder transferAccountCommandEncoder = new TransferAccountCommandEncoder();

    protected CommandBufferHandler wrapClusterConnection(CommandBufferHandler commandHandler) {
        return (buffer, offset) -> {
            if (ConnectionState.NOT_CONNECTED == connectionState) {
                tryConnectToCluster();
            }
            commandHandler.process(buffer, offset);
        };
    }

    @Override
    public void onMessage(int msgTypeId, MutableDirectBuffer buffer, int offset, int length) {
        messageHeaderDecoder.wrap(buffer, offset);

        log.debug("Received request with templateId={}", messageHeaderDecoder.templateId());
        handlers.getOrDefault(
            messageHeaderDecoder.templateId(),
            DEFAULT_NOT_FOUND_HANDLER
        ).process(buffer, offset);
    }

    @Override
    public int doWork() throws Exception {
        sendClusterHeartbeat();
        pollInboundRequestMessages();
        pollClusterEgressMessages();
        return noWorkAvailable();
    }

    @Override
    public String roleName() {
        return "account-agent";
    }

    private void sendClusterHeartbeat() {
        final long now = SystemEpochClock.INSTANCE.time();
        if (now >= lastHeartbeatTime + 250) {
            lastHeartbeatTime = now;
            if (connectionState == ConnectionState.CONNECTED) {
                aeronCluster.sendKeepAlive();
            }
        }
    }

    private void pollInboundRequestMessages() {
        commandBuffer.read(this);
    }

    private void pollClusterEgressMessages() {
        if (null != aeronCluster && !aeronCluster.isClosed()) {
            aeronCluster.pollEgress();
        }
    }

    private int noWorkAvailable() {
        return 0;
    }
}
