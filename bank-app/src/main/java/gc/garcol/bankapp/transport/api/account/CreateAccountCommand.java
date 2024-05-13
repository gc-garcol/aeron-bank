package gc.garcol.bankapp.transport.api.account;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAccountCommand {
    private final String correlationId = UUID.randomUUID().toString();
}
