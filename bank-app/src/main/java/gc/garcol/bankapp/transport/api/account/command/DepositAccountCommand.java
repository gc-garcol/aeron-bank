package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import lombok.Data;

@Data
public class DepositAccountCommand extends BaseCommand {
    private long accountId;
    private long amount;
}
