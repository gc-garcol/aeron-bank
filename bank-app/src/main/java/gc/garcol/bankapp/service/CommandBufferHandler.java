package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;

import java.util.function.BiConsumer;

/**
 * @author thaivc
 * @since 2024
 */
public interface CommandBufferHandler extends BiConsumer<MutableDirectBuffer, Integer> {
    default void process(MutableDirectBuffer buffer, int offset) {
        accept(buffer, offset);
    }

    CommandBufferHandler DEFAULT_NOT_FOUND_HANDLER = (buffer, offset) -> {
        throw new IllegalArgumentException("Unknown message type");
    };
}
