package gc.garcol.common.core;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@FunctionalInterface
public interface CommandHandlerMethod {
    void handle(DirectBuffer buffer, int offset);
}
