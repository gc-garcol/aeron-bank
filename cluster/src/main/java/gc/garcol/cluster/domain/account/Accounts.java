package gc.garcol.cluster.domain.account;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.common.exception.Bank4xxException;
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
public class Accounts implements AccountUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(Accounts.class);

    private final Long2ObjectHashMap<Account> accounts = new Long2ObjectHashMap<>();
    private final MutableLong idGenerator = new MutableLong(0);

    // Injected dependencies
    private final SessionMessageContext context;
    private final AccountClusterClientResponder accountClusterClientResponder;
    private final TimerManager timerManager;

    @Override
    public void openAccount(long accountId) {
        if (accounts.containsKey(accountId)) {
            LOGGER.warn("Account {} already exists", accountId);
            throw new Bank4xxException(String.format("Account %s already exists", accountId));
        }
        accounts.put(accountId, new Account(accountId));
    }

    @Override
    public void withdraw(long accountId, long amount) {
        if (!accounts.containsKey(accountId)) {
            LOGGER.warn("Withdrawn account {} does not exist", accountId);
            throw new Bank4xxException(String.format("Withdrawn account %s does not exist", accountId));
        }
        accounts.get(accountId).increase(amount);
    }

    @Override
    public void deposit(long accountId, long amount) {
        if (!accounts.containsKey(accountId)) {
            LOGGER.warn("Deposited account {} does not exist", accountId);
            throw new Bank4xxException(String.format("Deposited account %s does not exist", accountId));
        }
        accounts.get(accountId).decrease(amount);
    }

    @Override
    public void transfer(long fromAccountId, long toAccountId, long amount) {
        if (!accounts.containsKey(fromAccountId)) {
            LOGGER.warn("Transferring account {} does not exist", fromAccountId);
            throw new Bank4xxException(String.format("Transferring account %s does not exist", fromAccountId));
        }
        if (!accounts.containsKey(toAccountId)) {
            LOGGER.warn("Transferred account {} does not exist", toAccountId);
            throw new Bank4xxException(String.format("Transferred account %s does not exist", toAccountId));
        }
        var fromAccount = accounts.get(fromAccountId);
        if (fromAccount.getAmount() < amount) {
            LOGGER.warn("Transferring account {} does not have enough balance", fromAccountId);
            throw new Bank4xxException(String.format("Transferring account %s does not have enough balance", fromAccountId));
        }
        fromAccount.decrease(amount);
        accounts.get(toAccountId).increase(amount);
    }

    @Override
    public void transferMulti(long fromAccount, long[] toAccounts, long[] amounts) {
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
    public void closeAccount(long accountId) {
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
}
