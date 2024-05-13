package gc.garcol.bankapp.transport.api.account.command;

import lombok.Data;

import java.util.UUID;

@Data
public class DepositAccountCommand {

    private final String correlationId = UUID.randomUUID().toString();
    private long accountId;
    private long amount;

}
