package gc.garcol.bankcluster.domain.account;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountUseCase {
    void openAccount(String correlationId);

    void withdraw(String correlationId, long accountId, long amount);

    void deposit(String correlationId, long accountId, long amount);

    void transfer(String correlationId, long fromAccountId, long toAccountId, long amount);

    void transferMulti(String correlationId, long fromAccount, long[] toAccounts, long[] amounts);

    void closeAccount(String correlationId, long accountId);
}
