package gc.garcol.bankapp.service;

import gc.garcol.bankapp.service.constants.ServerConstant;
import gc.garcol.bankapp.transport.api.account.CreateAccountCommand;
import gc.garcol.bankapp.transport.api.account.DepositAccountCommand;
import gc.garcol.bankapp.transport.api.account.TransferBalanceCommand;
import gc.garcol.bankapp.transport.api.account.WithdrawAccountCommand;
import gc.garcol.common.exception.Bank5xxException;
import gc.garcol.protocol.MessageHeaderEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class AccountCommandDispatcherImpl extends AccountCommandDispatcherAbstract implements CommandBufferChannel {

    private final OneToOneRingBuffer commandBuffer;

    @Override
    public void createAccount(CreateAccountCommand command) {
        createAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        createAccountCommandEncoder.correlationId(command.getCorrelationId());
        offerRingBufferMessage(buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
    }

    @Override
    public void deposit(DepositAccountCommand command) {
        depositAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        depositAccountCommandEncoder.correlationId(command.getCorrelationId());
        depositAccountCommandEncoder.accountId(command.getAccountId());
        depositAccountCommandEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
    }

    @Override
    public void withdraw(WithdrawAccountCommand command) {
        withdrawAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        withdrawAccountCommandEncoder.correlationId(command.getCorrelationId());
        withdrawAccountCommandEncoder.accountId(command.getAccountId());
        withdrawAccountCommandEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
    }

    @Override
    public void transfer(TransferBalanceCommand command) {
        transferAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
        transferAccountCommandEncoder.correlationId(command.getCorrelationId());
        transferAccountCommandEncoder.fromAccountId(command.getFromAccountId());
        transferAccountCommandEncoder.toAccountId(command.getToAccountId());
        transferAccountCommandEncoder.amount(command.getAmount());
        offerRingBufferMessage(buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
    }

    private void offerRingBufferMessage(final ExpandableArrayBuffer buffer, final int offset,
                                        final int encodedLength) {
        final boolean success = commandBuffer.write(ServerConstant.MESSAGE_TYPE_ID, buffer, offset, encodedLength);
        if (!success) {
            throw new Bank5xxException("Failed to write to command buffer");
        }
    }
}
