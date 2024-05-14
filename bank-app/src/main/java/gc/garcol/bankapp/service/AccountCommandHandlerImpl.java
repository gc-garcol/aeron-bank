package gc.garcol.bankapp.service;

import lombok.extern.slf4j.Slf4j;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@Slf4j
public class AccountCommandHandlerImpl
    extends AccountCommandHandlerAbstract implements CommandBufferChannel
{

    public AccountCommandHandlerImpl(OneToOneRingBuffer commandBuffer) {
        super(commandBuffer);
    }

    @Override
    public void sendToClusterCreateAccountCommand(MutableDirectBuffer buffer, int offset) {
        createAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);

        createAccountCommandEncoder.wrapAndApplyHeader(buffer, offset, messageHeaderEncoder);
        createAccountCommandEncoder.correlationId(createAccountCommandBufferDecoder.correlationId());
    }

    @Override
    public void sendToClusterDepositAccountCommand(MutableDirectBuffer buffer, int offset) {
        depositAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);

        depositAccountCommandEncoder.wrapAndApplyHeader(buffer, offset, messageHeaderEncoder);
        depositAccountCommandEncoder.correlationId(depositAccountCommandBufferDecoder.correlationId());
        depositAccountCommandEncoder.accountId(depositAccountCommandBufferDecoder.accountId());
        depositAccountCommandEncoder.amount(depositAccountCommandBufferDecoder.amount());
    }

    @Override
    public void sendToClusterWithdrawAccountCommand(MutableDirectBuffer buffer, int offset) {
        withdrawAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);

        withdrawAccountCommandEncoder.wrapAndApplyHeader(buffer, offset, messageHeaderEncoder);
        withdrawAccountCommandEncoder.correlationId(withdrawAccountCommandBufferDecoder.correlationId());
        withdrawAccountCommandEncoder.accountId(withdrawAccountCommandBufferDecoder.accountId());
        withdrawAccountCommandEncoder.amount(withdrawAccountCommandBufferDecoder.amount());
    }

    @Override
    public void sendToClusterTransferBalanceCommand(MutableDirectBuffer buffer, int offset) {
        transferAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);

        transferAccountCommandEncoder.wrapAndApplyHeader(buffer, offset, messageHeaderEncoder);
        transferAccountCommandEncoder.correlationId(transferAccountCommandBufferDecoder.correlationId());
        transferAccountCommandEncoder.fromAccountId(transferAccountCommandBufferDecoder.fromAccountId());
        transferAccountCommandEncoder.toAccountId(transferAccountCommandBufferDecoder.toAccountId());
        transferAccountCommandEncoder.amount(transferAccountCommandBufferDecoder.amount());
    }

    @Override
    public void processConnectCluster(MutableDirectBuffer buffer, int offset) {

    }

    @Override
    public void processDisconnectCluster(MutableDirectBuffer buffer, int offset) {

    }
}
