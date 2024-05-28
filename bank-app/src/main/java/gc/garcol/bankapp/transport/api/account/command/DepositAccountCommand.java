package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class DepositAccountCommand extends BaseCommand {
    private long accountId;

    @Min(1)
    private long amount;
}
