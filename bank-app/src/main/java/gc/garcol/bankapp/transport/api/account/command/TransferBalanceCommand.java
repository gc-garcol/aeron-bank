package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author thaivc
 * @since 2024
 */
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true)
public class TransferBalanceCommand extends BaseCommand {
    private final long fromAccountId;
    private final long toAccountId;

    @Min(1)
    private final long amount;
}
