package gc.garcol.bankapp.service;

import gc.garcol.bankapp.service.constants.ServerConstant;
import gc.garcol.bankapp.transport.api.account.command.CreateAccountCommand;
import gc.garcol.bankapp.transport.api.account.command.DepositAccountCommand;
import gc.garcol.bankapp.transport.api.account.command.TransferBalanceCommand;
import gc.garcol.bankapp.transport.api.account.command.WithdrawAccountCommand;
import gc.garcol.common.exception.Bank5xxException;
import gc.garcol.protocol.MessageHeaderEncoder;
import org.agrona.ExpandableArrayBuffer;

public class AccountCommandDispatcherImpl extends AccountCommandDispatcherAbstract implements CommandBufferChannel {

    @Override
    public void createAccount(CreateAccountCommand command) {
        createAccountCommandBufferEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        createAccountCommandBufferEncoder.correlationId(command.getCorrelationId());
        offerRingBufferMessage(buffer, 0,
            MessageHeaderEncoder.ENCODED_LENGTH + createAccountCommandBufferEncoder.encodedLength());
    }

    @Override
    public void deposit(DepositAccountCommand command) {
        depositAccountCommandBufferEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        depositAccountCommandBufferEncoder.correlationId(command.getCorrelationId());
        depositAccountCommandBufferEncoder.accountId(command.getAccountId());
        depositAccountCommandBufferEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0,
            MessageHeaderEncoder.ENCODED_LENGTH + depositAccountCommandBufferEncoder.encodedLength());
    }

    @Override
    public void withdraw(WithdrawAccountCommand command) {
        withdrawAccountCommandBufferEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        withdrawAccountCommandBufferEncoder.correlationId(command.getCorrelationId());
        withdrawAccountCommandBufferEncoder.accountId(command.getAccountId());
        withdrawAccountCommandBufferEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0,
            MessageHeaderEncoder.ENCODED_LENGTH + withdrawAccountCommandBufferEncoder.encodedLength());
    }

    @Override
    public void transfer(TransferBalanceCommand command) {
        transferAccountCommandBufferEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        transferAccountCommandBufferEncoder.correlationId(command.getCorrelationId());
        transferAccountCommandBufferEncoder.fromAccountId(command.getFromAccountId());
        transferAccountCommandBufferEncoder.toAccountId(command.getToAccountId());
        transferAccountCommandBufferEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0,
            MessageHeaderEncoder.ENCODED_LENGTH + transferAccountCommandBufferEncoder.encodedLength());
    }

    private void offerRingBufferMessage(final ExpandableArrayBuffer buffer, final int offset,
                                        final int encodedLength) {
        final boolean success = commandBuffer.write(ServerConstant.MESSAGE_TYPE_ID, buffer, offset, encodedLength);
        if (!success) {
            throw new Bank5xxException("Failed to write to command buffer");
        }
    }
}
