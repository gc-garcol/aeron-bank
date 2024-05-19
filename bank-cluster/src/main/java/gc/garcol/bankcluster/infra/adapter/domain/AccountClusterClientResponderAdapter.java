package gc.garcol.bankcluster.infra.adapter.domain;

import gc.garcol.bankcluster.domain.SessionMessageContext;
import gc.garcol.bankcluster.domain.account.AccountResponseCode;
import gc.garcol.protocol.CommandResult;
import gc.garcol.protocol.MessageHeaderEncoder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class AccountClusterClientResponderAdapter extends AccountClusterClientResponderAbstract {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        AccountClusterClientResponderAdapter.class);

    private final SessionMessageContext context;


    @Override
    public void onAccountAdded(String correlationId, long accountId, AccountResponseCode result) {
        createAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .result(CommandResult.SUCCESS);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + createAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void rejectCreateAccount(String correlationId, AccountResponseCode result) {
        createAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .result(CommandResult.FAIL);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + createAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void onAccountWithdrawn(String correlationId, long accountId, long amount,
                                   AccountResponseCode result) {
        withdrawAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .withdrawAmount(amount)
            .result(CommandResult.SUCCESS);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + withdrawAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void rejectWithdrawAccount(String correlationId, long accountId, long amount,
                                      AccountResponseCode result) {
        withdrawAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .withdrawAmount(amount)
            .result(CommandResult.FAIL);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + withdrawAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void onAccountDeposited(String correlationId, long accountId, long amount,
                                   AccountResponseCode result) {
        depositAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .depositAmount(amount)
            .result(CommandResult.SUCCESS);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + depositAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void rejectDepositAccount(String correlationId, long accountId, long amount,
                                     AccountResponseCode result) {
        depositAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .accountId(accountId)
            .depositAmount(amount)
            .result(CommandResult.FAIL);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + depositAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void onAccountTransferred(String correlationId, long fromAccountId, long toAccountId,
                                     long amount, AccountResponseCode result) {
        transferAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .fromAccountId(fromAccountId)
            .toAccountId(toAccountId)
            .transferredAmount(amount)
            .result(CommandResult.SUCCESS);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + transferAccountResultEncoder.encodedLength()
        );
    }

    @Override
    public void rejectTransferAccount(String correlationId, long fromAccountId, long toAccountId,
                                      long amount, AccountResponseCode result) {
        transferAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
            .correlationId(correlationId)
            .fromAccountId(fromAccountId)
            .toAccountId(toAccountId);
        context.reply(
            buffer,
            0,
            MessageHeaderEncoder.ENCODED_LENGTH + transferAccountResultEncoder.encodedLength()
        );
    }
}
