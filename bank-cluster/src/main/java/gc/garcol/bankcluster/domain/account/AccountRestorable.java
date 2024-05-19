package gc.garcol.bankcluster.domain.account;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountRestorable {

    /**
     * Loads an account from the snapshot
     *
     * @param accountId
     * @param amount
     * @param active
     */
    void restoreAccount(long accountId, long amount, boolean active);

    /**
     * Restores the account id generator from snapshot
     *
     * @param accountId the auction id
     */
    void restoreAutoIdGenerator(final long accountId);
}
