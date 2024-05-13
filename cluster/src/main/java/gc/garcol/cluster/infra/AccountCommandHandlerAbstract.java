package gc.garcol.cluster.infra;

import gc.garcol.protocol.*;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AccountCommandHandlerAbstract implements AccountCommandHandler {
    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();
    protected final WithdrawAccountCommandDecoder withdrawAccountCommandDecoder = new WithdrawAccountCommandDecoder();
    protected final DepositAccountCommandDecoder depositAccountCommandDecoder = new DepositAccountCommandDecoder();
    protected final CreateAccountCommandDecoder createAccountCommandDecoder = new CreateAccountCommandDecoder();
    protected final TransferAccountCommandDecoder transferAccountCommandDecoder = new TransferAccountCommandDecoder();
}
