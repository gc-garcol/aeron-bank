package gc.garcol.bankapp.service;

import gc.garcol.protocol.*;
import lombok.Setter;
import org.agrona.ExpandableArrayBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AccountCommandDispatcherAbstract implements AccountCommandDispatcher {
    @Setter
    protected OneToOneRingBuffer commandBuffer;

    protected final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(1 << 10);
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final CreateAccountCommandBufferEncoder createAccountCommandBufferEncoder = new CreateAccountCommandBufferEncoder();
    protected final DepositAccountCommandBufferEncoder depositAccountCommandBufferEncoder = new DepositAccountCommandBufferEncoder();
    protected final WithdrawAccountCommandBufferEncoder withdrawAccountCommandBufferEncoder = new WithdrawAccountCommandBufferEncoder();
    protected final TransferAccountCommandBufferEncoder transferAccountCommandBufferEncoder = new TransferAccountCommandBufferEncoder();
}
