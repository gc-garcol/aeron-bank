package gc.garcol.bankcluster.infra;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountCommandHandler {
    void createAccountCommandHandler(DirectBuffer buffer, int offset);
    void depositAccountCommandHandler(DirectBuffer buffer, int offset);
    void withdrawAccountCommandHandler(DirectBuffer buffer, int offset);
    void transferAccountCommandHandler(DirectBuffer buffer, int offset);
}
