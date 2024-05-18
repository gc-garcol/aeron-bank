package gc.garcol.bankcluster.infra.adapter.domain;

import gc.garcol.bankcluster.domain.account.AccountClusterClientResponder;
import gc.garcol.protocol.*;
import org.agrona.ExpandableDirectByteBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AccountClusterClientResponderAbstract implements AccountClusterClientResponder {
    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1024);
    protected final CreateAccountResultEncoder createAccountResultEncoder = new CreateAccountResultEncoder();
    protected final DepositAccountResultEncoder depositAccountResultEncoder = new DepositAccountResultEncoder();
    protected final WithdrawAccountResultEncoder withdrawAccountResultEncoder = new WithdrawAccountResultEncoder();
    protected final TransferAccountResultEncoder transferAccountResultEncoder = new TransferAccountResultEncoder();
}
