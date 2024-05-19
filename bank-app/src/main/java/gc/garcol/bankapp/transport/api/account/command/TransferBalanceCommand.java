package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author thaivc
 * @since 2024
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class TransferBalanceCommand extends BaseCommand {
    private final long fromAccountId;
    private final long toAccountId;
    private final long amount;
}
