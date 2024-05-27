package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DepositAccountCommand extends BaseCommand {
    private long accountId;

    @Min(1)
    private long amount;
}
