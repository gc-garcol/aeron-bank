package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DepositAccountCommand extends BaseCommand {
    private long accountId;
    private long amount;
}
