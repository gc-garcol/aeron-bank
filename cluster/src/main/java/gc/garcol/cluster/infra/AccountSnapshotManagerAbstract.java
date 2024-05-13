package gc.garcol.cluster.infra;

import gc.garcol.common.core.SnapshotManager;
import gc.garcol.protocol.*;
import org.agrona.ExpandableDirectByteBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AccountSnapshotManagerAbstract implements SnapshotManager, AccountSnapshotManager {
    protected final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1 << 10);
    protected final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    protected final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();

    protected final AccountSnapshotEncoder accountSnapshotEncoder = new AccountSnapshotEncoder();
    protected final AccountSnapshotDecoder accountSnapshotDecoder = new AccountSnapshotDecoder();
    protected final AccountIdSnapshotDecoder accountIdSnapshotDecoder = new AccountIdSnapshotDecoder();
    protected final AccountIdSnapshotEncoder accountIdSnapshotEncoder = new AccountIdSnapshotEncoder();
    protected final EndOfSnapshotEncoder endOfSnapshotEncoder = new EndOfSnapshotEncoder();
}
