package gc.garcol.bankapp.transport.api.account;

import lombok.Data;

import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
@Data
public class TransferBalanceCommand {
    private final String correlationId = UUID.randomUUID().toString();
    private final long fromAccountId;
    private final long toAccountId;
    private final long amount;
}
