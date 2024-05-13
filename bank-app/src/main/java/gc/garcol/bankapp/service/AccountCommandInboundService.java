package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.api.account.CreateAccountCommand;
import gc.garcol.bankapp.transport.api.account.DepositAccountCommand;
import gc.garcol.protocol.AddAccountCommandEncoder;
import gc.garcol.protocol.DepositAccountCommandEncoder;
import gc.garcol.protocol.MessageHeaderEncoder;
import lombok.RequiredArgsConstructor;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCommandInboundService {

  private final OneToOneRingBuffer commandBuffer;

  private final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(1024);
  private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
  private final AddAccountCommandEncoder addAccountCommandEncoder = new AddAccountCommandEncoder();
  private final DepositAccountCommandEncoder depositAccountCommandEncoder = new DepositAccountCommandEncoder();

  private void offerRingBufferMessage(final ExpandableArrayBuffer buffer, final int offset,
      final int encodedLength) {
    final boolean success = commandBuffer.write(10, buffer, offset, encodedLength);
    if (!success) {
      throw new RuntimeException("Failed to write to command buffer");
    }
  }

  public void addAccount(CreateAccountCommand command) {
    addAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
    addAccountCommandEncoder.correlationId(command.correlationId());
    offerRingBufferMessage(buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
  }

  public void deposit(DepositAccountCommand command) {
      
  }
}
