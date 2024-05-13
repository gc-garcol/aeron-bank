package gc.garcol.bankapp.service;

import gc.garcol.protocol.*;
import org.agrona.ExpandableArrayBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AccountCommandDispatcherAbstract implements AccountCommandDispatcher {
    protected final ExpandableArrayBuffer buffer = new ExpandableArrayBuffer(1 << 10);
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final CreateAccountCommandBufferEncoder createAccountCommandBufferEncoder = new CreateAccountCommandBufferEncoder();
    protected final DepositAccountCommandBufferEncoder depositAccountCommandBufferEncoder = new DepositAccountCommandBufferEncoder();
    protected final WithdrawAccountCommandBufferEncoder withdrawAccountCommandBufferEncoder = new WithdrawAccountCommandBufferEncoder();
    protected final TransferAccountCommandBufferEncoder transferAccountCommandBufferEncoder = new TransferAccountCommandBufferEncoder();
}
