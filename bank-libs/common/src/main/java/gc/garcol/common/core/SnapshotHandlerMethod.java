package gc.garcol.common.core;

import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@FunctionalInterface
public interface SnapshotHandlerMethod {

    void handle(DirectBuffer buffer, final int offset, final int length, final Header header);
}
