package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.api.account.CreateAccountCommand;
import gc.garcol.protocol.AddAccountCommandEncoder;
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

  public void addAccount(CreateAccountCommand command) {
    addAccountCommandEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder);
    addAccountCommandEncoder.correlationId(command.correlationId());
    boolean success = commandBuffer.write(10, buffer, 0, MessageHeaderEncoder.ENCODED_LENGTH);
    if (!success) {
        throw new RuntimeException("Failed to write to command buffer");
    }
  }
}
