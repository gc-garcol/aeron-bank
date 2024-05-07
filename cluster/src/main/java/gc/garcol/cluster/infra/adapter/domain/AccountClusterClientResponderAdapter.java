package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.account.Account;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.AccountResponseCode;
import gc.garcol.protocol.AddAccountResultEncoder;
import gc.garcol.protocol.CommandResult;
import gc.garcol.protocol.MessageHeaderEncoder;
import lombok.RequiredArgsConstructor;
import org.agrona.ExpandableDirectByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class AccountClusterClientResponderAdapter implements AccountClusterClientResponder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountClusterClientResponderAdapter.class);

    private final SessionMessageContext context;

    private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    private final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1024);
    private final AddAccountResultEncoder addAccountResultEncoder = new AddAccountResultEncoder();
    private final AddAccountResultEncoder rejectAddAccountResultEncoder = new AddAccountResultEncoder();

    @Override
    public void onAccountAdded(String correlationId, long accountId, AccountResponseCode result) {
        addAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .result(CommandResult.SUCCESS);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + addAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void rejectAddAccount(String correlationId, AccountResponseCode result) {
        rejectAddAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .result(CommandResult.FAIL);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + rejectAddAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void onAccountWithdrawn(String correlationId, long accountId, long amount, AccountResponseCode result) {

    }

    @Override
    public void rejectWithdrawAccount(String correlationId, long accountId, long amount, AccountResponseCode result) {

    }

    @Override
    public void onAccountDeposited(String correlationId, long accountId, long amount, AccountResponseCode result) {

    }

    @Override
    public void rejectDepositAccount(String correlationId, long accountId, long amount, AccountResponseCode result) {

    }

    @Override
    public void onAccountTransferred(String correlationId, long fromAccountId, long toAccountId, long amount, AccountResponseCode result) {

    }

    @Override
    public void rejectTransferAccount(String correlationId, long fromAccountId, long toAccountId, long amount, AccountResponseCode result) {

    }
}
