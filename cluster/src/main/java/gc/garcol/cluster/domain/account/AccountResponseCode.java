package gc.garcol.cluster.domain.account;

/**
 * @author thaivc
 * @since 2024
 */
public enum AccountResponseCode {
    ADD_ACCOUNT_SUCCESS,
    ACCOUNT_ALREADY_EXISTS,
    ACCOUNT_NOT_EXISTED,
    WITHDRAW_ACCOUNT_SUCCESS,
    BALANCE_NOT_ENOUGH,
    DEPOSIT_ACCOUNT_SUCCESS,
    DEPOSIT_ACCOUNT_FAILED,
    TRANSFER_SUCCESS,
    TRANSFER_FAILED
}
