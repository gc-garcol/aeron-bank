package gc.garcol.cluster.domain.account;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountUseCase {
    void openAccount(long accountId);

    void withdraw(long accountId, long amount);

    void deposit(long accountId, long amount);

    void transfer(long fromAccountId, long toAccountId, long amount);

    void transferMulti(long fromAccount, long[] toAccounts, long[] amounts);

    void closeAccount(long accountId);
}
