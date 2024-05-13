package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.AccountResponseCode;
import gc.garcol.protocol.AddAccountResultEncoder;
import gc.garcol.protocol.CommandResult;
import gc.garcol.protocol.DepositAccountResultEncoder;
import gc.garcol.protocol.MessageHeaderEncoder;
import gc.garcol.protocol.TransferAccountResultEncoder;
import gc.garcol.protocol.WithdrawAccountResultEncoder;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(
      AccountClusterClientResponderAdapter.class);

  private final SessionMessageContext context;

  private final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
  private final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1024);
  private final AddAccountResultEncoder addAccountResultEncoder = new AddAccountResultEncoder();
  private final DepositAccountResultEncoder depositAccountResultEncoder = new DepositAccountResultEncoder();
  private final WithdrawAccountResultEncoder withdrawAccountResultEncoder = new WithdrawAccountResultEncoder();
  private final TransferAccountResultEncoder transferAccountResultEncoder = new TransferAccountResultEncoder();

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
    addAccountResultEncoder.wrapAndApplyHeader(buffer, 0, messageHeaderEncoder)
        .correlationId(correlationId)
        .result(CommandResult.FAIL);
    context.reply(
        buffer,
        0,
        MessageHeaderEncoder.ENCODED_LENGTH + addAccountResultEncoder.encodedLength()
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
