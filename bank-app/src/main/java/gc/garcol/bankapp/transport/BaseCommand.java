package gc.garcol.bankapp.transport;

import lombok.Getter;

import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class BaseCommand {
    {
        this.correlationId = UUID.randomUUID().toString();
    }

    @Getter
    protected String correlationId;
}
