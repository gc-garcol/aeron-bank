package gc.garcol.cluster.domain.account;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountClusterClientResponder {

    void onAccountAdded(String correlationId, long accountId, AccountResponseCode result);

    void rejectCreateAccount(String correlationId, AccountResponseCode result);

    void onAccountWithdrawn(String correlationId, long accountId, long amount,
                            AccountResponseCode result);

    void rejectWithdrawAccount(String correlationId, long accountId, long amount,
                               AccountResponseCode result);

    void onAccountDeposited(String correlationId, long accountId, long amount,
                            AccountResponseCode result);

    void rejectDepositAccount(String correlationId, long accountId, long amount,
                              AccountResponseCode result);

    void onAccountTransferred(String correlationId, long fromAccountId, long toAccountId, long amount,
                              AccountResponseCode result);

    void rejectTransferAccount(String correlationId, long fromAccountId, long toAccountId,
                               long amount, AccountResponseCode result);
}
