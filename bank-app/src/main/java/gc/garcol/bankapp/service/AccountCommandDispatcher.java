package gc.garcol.bankapp.service;

import gc.garcol.bankapp.transport.api.account.command.CreateAccountCommand;
import gc.garcol.bankapp.transport.api.account.command.DepositAccountCommand;
import gc.garcol.bankapp.transport.api.account.command.TransferBalanceCommand;
import gc.garcol.bankapp.transport.api.account.command.WithdrawAccountCommand;

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
