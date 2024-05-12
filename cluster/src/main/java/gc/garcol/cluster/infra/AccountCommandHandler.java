package gc.garcol.cluster.infra;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountCommandHandler {
    void addAccountCommandHandler(DirectBuffer buffer, int offset);
    void depositAccountCommandHandler(DirectBuffer buffer, int offset);
    void withdrawAccountCommandHandler(DirectBuffer buffer, int offset);
    void transferAccountCommandHandler(DirectBuffer buffer, int offset);
}
