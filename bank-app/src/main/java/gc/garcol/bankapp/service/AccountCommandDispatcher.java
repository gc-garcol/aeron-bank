package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.api.account.CreateAccountCommand;
import gc.garcol.bankapp.transport.api.account.DepositAccountCommand;
import gc.garcol.bankapp.transport.api.account.TransferBalanceCommand;
import gc.garcol.bankapp.transport.api.account.WithdrawAccountCommand;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountCommandDispatcher {
    void createAccount(CreateAccountCommand command);
    void deposit(DepositAccountCommand command);
    void withdraw(WithdrawAccountCommand command);
    void transfer(TransferBalanceCommand command);
}
