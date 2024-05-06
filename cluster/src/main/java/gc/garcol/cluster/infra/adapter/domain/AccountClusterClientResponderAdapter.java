package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.AccountResponseCode;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void onAccountAdded(String correlationId, long accountId, AccountResponseCode result) {

    }

    @Override
    public void rejectAddAccount(String correlationId, AccountResponseCode result) {

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
