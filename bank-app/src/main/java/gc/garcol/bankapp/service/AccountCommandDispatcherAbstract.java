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
    protected final CreateAccountCommandEncoder createAccountCommandEncoder = new CreateAccountCommandEncoder();
    protected final DepositAccountCommandEncoder depositAccountCommandEncoder = new DepositAccountCommandEncoder();
    protected final WithdrawAccountCommandEncoder withdrawAccountCommandEncoder = new WithdrawAccountCommandEncoder();
    protected final TransferAccountCommandEncoder transferAccountCommandEncoder = new TransferAccountCommandEncoder();
}
