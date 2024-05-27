package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.BaseError;
import gc.garcol.bankapp.transport.api.account.command.CreateAccountResponse;
import gc.garcol.bankapp.transport.api.account.command.DepositAccountResponse;
import gc.garcol.bankapp.transport.api.account.command.TransferBalanceResponse;
import gc.garcol.bankapp.transport.api.account.command.WithdrawAccountResponse;
import gc.garcol.protocol.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountClusterEgressListenerImpl extends AccountClusterEgressListenerAbstract {
    
    private final SimpleAccountRequestReplyFuture accountRequestReplyFuture;

    @Override
    public void processCreateAccount(DirectBuffer buffer, int offset) {
        createAccountResultDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        if (createAccountResultDecoder.result() == CommandResult.SUCCESS) {
            var response = new CreateAccountResponse();
            response.setCorrelationId(createAccountResultDecoder.correlationId());
            response.setAccountId(createAccountResultDecoder.accountId());
            log.debug("[processCreateAccount] On receive reply {}", response);
            accountRequestReplyFuture.replySuccess(
                response.getCorrelationId(),
                response
            );
        } else {
            log.debug("[processCreateAccount] {} - Create account failed: {}",
                createAccountResultDecoder.correlationId(), createAccountResultDecoder.result());
            accountRequestReplyFuture.replyFail(
                createAccountResultDecoder.correlationId(),
                new BaseError("Create account failed - " + createAccountResultDecoder.result().name())
            );
        }
    }

    @Override
    public void processWithdrawAccount(DirectBuffer buffer, int offset) {
        withdrawAccountResultDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        if (withdrawAccountResultDecoder.result() == CommandResult.SUCCESS) {
            var response = new WithdrawAccountResponse();
            response.setAccountId(withdrawAccountResultDecoder.accountId());
            response.setCorrelationId(withdrawAccountResultDecoder.correlationId());
            response.setWithdrawAmount(withdrawAccountResultDecoder.withdrawAmount());
            response.setCurrentAmount(withdrawAccountResultDecoder.balance());
            log.debug("[processWithdrawAccount] On receive reply {}", response);
            accountRequestReplyFuture.replySuccess(
                withdrawAccountResultDecoder.correlationId(),
                response
            );
        } else {
            log.debug("[processWithdrawAccount] {} - Withdraw account failed: {}",
                withdrawAccountResultDecoder.correlationId(), withdrawAccountResultDecoder.result());
            accountRequestReplyFuture.replyFail(
                withdrawAccountResultDecoder.correlationId(),
                new BaseError("Withdraw account failed - " + withdrawAccountResultDecoder.result().name())
            );
        }
    }

    @Override
    public void processDepositAccount(DirectBuffer buffer, int offset) {
        depositAccountResultDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        if (depositAccountResultDecoder.result() == CommandResult.SUCCESS) {
            var response = new DepositAccountResponse();
            response.setAccountId(depositAccountResultDecoder.accountId());
            response.setCorrelationId(depositAccountResultDecoder.correlationId());
            response.setDepositAmount(depositAccountResultDecoder.depositAmount());
            response.setCurrentAmount(depositAccountResultDecoder.balance());
            log.debug("[processDepositAccount] On receive reply {}", response);
            accountRequestReplyFuture.replySuccess(
                depositAccountResultDecoder.correlationId(),
                response
            );
        } else {
            log.debug("[processDepositAccount] {} - Deposit account failed: {}",
                depositAccountResultDecoder.correlationId(), depositAccountResultDecoder.result());
            accountRequestReplyFuture.replyFail(
                depositAccountResultDecoder.correlationId(),
                new BaseError("Deposit account failed - " + depositAccountResultDecoder.result().name())
            );
        }
    }

    @Override
    public void processTransferAccount(DirectBuffer buffer, int offset) {
        transferAccountResultDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        if (transferAccountResultDecoder.result() == CommandResult.SUCCESS) {
            var response = new TransferBalanceResponse();
            response.setCorrelationId(transferAccountResultDecoder.correlationId());
            response.setToAccountId(transferAccountResultDecoder.toAccountId());
            response.setFromAccountId(transferAccountResultDecoder.fromAccountId());
            response.setTransferAmount(transferAccountResultDecoder.transferredAmount());
            response.setCurrentAmount(transferAccountResultDecoder.balance());
            log.debug("[processTransferAccount] On receive reply {}", response);
            accountRequestReplyFuture.replySuccess(
                transferAccountResultDecoder.correlationId(),
                response
            );
        } else {
            log.debug("[processTransferAccount] {} - Transfer account failed: {}",
                transferAccountResultDecoder.correlationId(), transferAccountResultDecoder.result());
            accountRequestReplyFuture.replyFail(
                transferAccountResultDecoder.correlationId(),
                new BaseError("Transfer account failed - " + transferAccountResultDecoder.result().name())
            );
        }
    }
}
