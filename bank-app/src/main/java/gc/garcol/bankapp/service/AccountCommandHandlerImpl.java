package gc.garcol.bankapp.service;

import gc.garcol.protocol.*;
import lombok.extern.slf4j.Slf4j;
import org.agrona.MutableDirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@Slf4j
public class AccountCommandHandlerImpl
    extends AccountCommandHandlerAbstract implements CommandBufferChannel {

    {
        handlers.put(ConnectClusterDecoder.TEMPLATE_ID, this::processConnectCluster);
        handlers.put(DisconnectClusterDecoder.TEMPLATE_ID, this::processDisconnectCluster);
        handlers.put(CreateAccountCommandBufferDecoder.TEMPLATE_ID, wrapClusterConnection(this::sendToClusterCreateAccountCommand));
        handlers.put(DepositAccountCommandBufferDecoder.TEMPLATE_ID, wrapClusterConnection(this::sendToClusterDepositAccountCommand));
        handlers.put(WithdrawAccountCommandBufferDecoder.TEMPLATE_ID, wrapClusterConnection(this::sendToClusterWithdrawAccountCommand));
        handlers.put(TransferAccountCommandBufferDecoder.TEMPLATE_ID, wrapClusterConnection(this::sendToClusterTransferBalanceCommand));
    }

    @Override
    public void sendToClusterCreateAccountCommand(MutableDirectBuffer buffer, int offset) {
        createAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        log.debug("offer CreateAccountCommand, (correlationId: {})", createAccountCommandBufferDecoder.correlationId());

        createAccountCommandEncoder.wrapAndApplyHeader(sendBuffer, 0, messageHeaderEncoder);
        createAccountCommandEncoder.correlationId(createAccountCommandBufferDecoder.correlationId());
        publishCommandToCluster(
            sendBuffer, 0, MessageHeaderEncoder.ENCODED_LENGTH + createAccountCommandEncoder.encodedLength());
    }

    @Override
    public void sendToClusterDepositAccountCommand(MutableDirectBuffer buffer, int offset) {
        depositAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        log.debug("offer DepositAccountCommand, (correlationId: {}, accountId: {}, amount: {})",
            depositAccountCommandBufferDecoder.correlationId(),
            depositAccountCommandBufferDecoder.accountId(),
            depositAccountCommandBufferDecoder.amount());

        depositAccountCommandEncoder.wrapAndApplyHeader(sendBuffer, 0, messageHeaderEncoder);
        depositAccountCommandEncoder.correlationId(depositAccountCommandBufferDecoder.correlationId());
        depositAccountCommandEncoder.accountId(depositAccountCommandBufferDecoder.accountId());
        depositAccountCommandEncoder.amount(depositAccountCommandBufferDecoder.amount());
        publishCommandToCluster(
            sendBuffer, 0, MessageHeaderEncoder.ENCODED_LENGTH + depositAccountCommandEncoder.encodedLength());
    }

    @Override
    public void sendToClusterWithdrawAccountCommand(MutableDirectBuffer buffer, int offset) {
        withdrawAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        log.debug("offer WithdrawAccountCommand, (correlationId: {}, accountId: {}, amount: {})",
            withdrawAccountCommandBufferDecoder.correlationId(),
            withdrawAccountCommandBufferDecoder.accountId(),
            withdrawAccountCommandBufferDecoder.amount());

        withdrawAccountCommandEncoder.wrapAndApplyHeader(sendBuffer, 0, messageHeaderEncoder);
        withdrawAccountCommandEncoder.correlationId(withdrawAccountCommandBufferDecoder.correlationId());
        withdrawAccountCommandEncoder.accountId(withdrawAccountCommandBufferDecoder.accountId());
        withdrawAccountCommandEncoder.amount(withdrawAccountCommandBufferDecoder.amount());
        publishCommandToCluster(
            sendBuffer, 0, MessageHeaderEncoder.ENCODED_LENGTH + withdrawAccountCommandEncoder.encodedLength());
    }

    @Override
    public void sendToClusterTransferBalanceCommand(MutableDirectBuffer buffer, int offset) {
        transferAccountCommandBufferDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        log.debug("offer TransferBalanceCommand, (correlationId: {}, from: {}, to: {}, amount: {})",
            transferAccountCommandBufferDecoder.correlationId(),
            transferAccountCommandBufferDecoder.fromAccountId(),
            transferAccountCommandBufferDecoder.toAccountId(),
            transferAccountCommandBufferDecoder.amount());

        transferAccountCommandEncoder.wrapAndApplyHeader(sendBuffer, 0, messageHeaderEncoder);
        transferAccountCommandEncoder.correlationId(transferAccountCommandBufferDecoder.correlationId());
        transferAccountCommandEncoder.fromAccountId(transferAccountCommandBufferDecoder.fromAccountId());
        transferAccountCommandEncoder.toAccountId(transferAccountCommandBufferDecoder.toAccountId());
        transferAccountCommandEncoder.amount(transferAccountCommandBufferDecoder.amount());
        publishCommandToCluster(
            sendBuffer, 0, MessageHeaderEncoder.ENCODED_LENGTH + transferAccountCommandEncoder.encodedLength());
    }
}
