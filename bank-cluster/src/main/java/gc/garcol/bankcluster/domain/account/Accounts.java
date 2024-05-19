package gc.garcol.bankcluster.domain.account;

import gc.garcol.bankcluster.domain.SessionMessageContext;
import gc.garcol.bankcluster.domain.TimerManager;
import gc.garcol.common.exception.Bank4xxException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.agrona.collections.Long2ObjectHashMap;
import org.agrona.collections.MutableLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class Accounts implements AccountUseCase, AccountRestorable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Accounts.class);

    @Getter
    private final Long2ObjectHashMap<Account> accounts = new Long2ObjectHashMap<>();

    @Getter
    private final MutableLong idGenerator = new MutableLong(0);

    // Injected dependencies
    private final SessionMessageContext context;
    private final AccountClusterClientResponder accountClusterClientResponder;
    private final TimerManager timerManager;

    @Override
    public void openAccount(String correlationId) {
        LOGGER.debug("[Start] Create account: (correlationId: {})", correlationId);
        long accountId = idGenerator.getAndIncrement();
        accounts.put(accountId, new Account(accountId));
        accountClusterClientResponder.onAccountAdded(
            correlationId, accountId, AccountResponseCode.ADD_ACCOUNT_SUCCESS
        );
        LOGGER.debug("[End] Create account: (correlationId: {})", correlationId);
    }

    @Override
    public void withdraw(String correlationId, long accountId, long amount) {
        LOGGER.debug("[Start] Withdraw account: (correlationId: {}, accountId: {}, amount: {})", correlationId, accountId, amount);
        if (!accounts.containsKey(accountId)) {
            LOGGER.warn("Withdrawn account {} does not exist", accountId);
            accountClusterClientResponder.rejectWithdrawAccount(
                correlationId, accountId, amount, AccountResponseCode.ACCOUNT_NOT_EXISTED
            );
            throw new Bank4xxException(String.format("Withdrawn account %s does not exist", accountId));
        }
        if (accounts.get(accountId).getAmount() < amount) {
            LOGGER.warn("Withdrawn account {} does not have enough balance", accountId);
            accountClusterClientResponder.rejectWithdrawAccount(
                correlationId, accountId, amount, AccountResponseCode.BALANCE_NOT_ENOUGH
            );
            throw new Bank4xxException(String.format("Withdrawn account %s does not have enough balance", accountId));
        }
        accounts.get(accountId).increase(amount);
        accountClusterClientResponder.onAccountWithdrawn(
            correlationId, accountId, amount, AccountResponseCode.WITHDRAW_ACCOUNT_SUCCESS
        );
        LOGGER.debug("[End] Withdraw account: (correlationId: {}, accountId: {}, amount: {})", correlationId, accountId, amount);
    }

    @Override
    public void deposit(String correlationId, long accountId, long amount) {
        LOGGER.debug("[Start] Deposit account: (correlationId: {}, accountId: {}, amount: {})", correlationId, accountId, amount);
        if (!accounts.containsKey(accountId)) {
            LOGGER.warn("Deposited account {} does not exist", accountId);
            accountClusterClientResponder.rejectDepositAccount(
                correlationId, accountId, amount, AccountResponseCode.ACCOUNT_NOT_EXISTED
            );
            throw new Bank4xxException(String.format("Deposited account %s does not exist", accountId));
        }
        accounts.get(accountId).decrease(amount);
        accountClusterClientResponder.onAccountDeposited(
            correlationId, accountId, amount, AccountResponseCode.DEPOSIT_ACCOUNT_SUCCESS
        );
        LOGGER.debug("[End] Deposit account: (correlationId: {}, accountId: {}, amount: {})", correlationId, accountId, amount);
    }

    @Override
    public void transfer(String correlationId, long fromAccountId, long toAccountId, long amount) {
        LOGGER.debug("[Start] transfer: (correlationId: {}, fromAccountId: {}, toAccountId: {}, amount: {})", correlationId, fromAccountId, toAccountId, amount);
        if (!accounts.containsKey(fromAccountId)) {
            LOGGER.warn("Transferring account {} does not exist", fromAccountId);
            accountClusterClientResponder.rejectTransferAccount(
                correlationId, fromAccountId, toAccountId, amount, AccountResponseCode.ACCOUNT_NOT_EXISTED
            );
            throw new Bank4xxException(String.format("Transferring account %s does not exist", fromAccountId));
        }
        if (!accounts.containsKey(toAccountId)) {
            LOGGER.warn("Transferred account {} does not exist", toAccountId);
            accountClusterClientResponder.rejectTransferAccount(
                correlationId, fromAccountId, toAccountId, amount, AccountResponseCode.ACCOUNT_NOT_EXISTED
            );
            throw new Bank4xxException(String.format("Transferred account %s does not exist", toAccountId));
        }
        var fromAccount = accounts.get(fromAccountId);
        if (fromAccount.getAmount() < amount) {
            LOGGER.warn("Transferring account {} does not have enough balance", fromAccountId);
            accountClusterClientResponder.rejectTransferAccount(
                correlationId, fromAccountId, toAccountId, amount, AccountResponseCode.BALANCE_NOT_ENOUGH
            );
            throw new Bank4xxException(String.format("Transferring account %s does not have enough balance", fromAccountId));
        }
        fromAccount.decrease(amount);
        accounts.get(toAccountId).increase(amount);
        accountClusterClientResponder.onAccountTransferred(
            correlationId, fromAccountId, toAccountId, amount, AccountResponseCode.TRANSFER_SUCCESS
        );
        LOGGER.debug("[End] transfer: (correlationId: {}, fromAccountId: {}, toAccountId: {}, amount: {})", correlationId, fromAccountId, toAccountId, amount);
    }

    @Override
    public void transferMulti(String correlationId, long fromAccount, long[] toAccounts, long[] amounts) {
        if (!accounts.containsKey(fromAccount)) {
            LOGGER.warn("Transferring account {} does not exist", fromAccount);
            throw new Bank4xxException(String.format("Transferring account %s does not exist", fromAccount));
        }
        long totalTransferAmount = Arrays.stream(amounts).sum();
        if (accounts.get(fromAccount).getAmount() < totalTransferAmount) {
            LOGGER.warn("Transferring account {} does not have enough balance", fromAccount);
            throw new Bank4xxException(String.format("Transferring account %s does not have enough balance", fromAccount));
        }
        for (long toAccount : toAccounts) {
            if (!accounts.containsKey(toAccount)) {
                LOGGER.warn("Transferred account {} does not exist", toAccount);
                throw new Bank4xxException(String.format("Transferred account %s does not exist", toAccount));
            }
        }
        for (int i = 0; i < toAccounts.length; i++) {
            accounts.get(fromAccount).decrease(amounts[i]);
            accounts.get(toAccounts[i]).increase(amounts[i]);
        }
    }

    @Override
    public void closeAccount(String correlationId, long accountId) {
        if (!accounts.containsKey(accountId)) {
            LOGGER.warn("Account {} does not exist", accountId);
            throw new Bank4xxException(String.format("Account %s does not exist", accountId));
        }
        if (!accounts.get(accountId).isActive()) {
            LOGGER.warn("Account {} is already closed", accountId);
            throw new Bank4xxException(String.format("Account %s is already closed", accountId));
        }
        accounts.get(accountId).setActive(false);
    }

    @Override
    public void restoreAccount(long accountId, long amount, boolean active) {
        final Account account = new Account(accountId);
        account.setAmount(amount);
        account.setActive(active);
        accounts.put(accountId, account);
    }

    @Override
    public void restoreAutoIdGenerator(final long accountId) {
        LOGGER.info("Restoring auto id generator to {}", accountId);
        idGenerator.set(accountId);
    }
}
