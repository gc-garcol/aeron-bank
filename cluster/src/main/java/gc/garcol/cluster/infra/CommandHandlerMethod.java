package gc.garcol.cluster.infra;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface CommandHandlerMethod {
    void handle(DirectBuffer buffer, int offset);
}
