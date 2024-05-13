package gc.garcol.cluster.infra;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface AccountSnapshotManager {
    void onAccountSnapshot(final DirectBuffer buffer, final int offset, final int length);
    void onAccountIdGeneratorSnapshot(final DirectBuffer buffer, final int offset, final int length);
    void onEndOfSnapshot(final DirectBuffer buffer, final int offset, final int length);
}
