package gc.garcol.common.core;

import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.IdleStrategy;

/**
 * @author thaivc
 * @since 2024
 */
public interface SnapshotManager extends FragmentHandler {

    /**
     * Provide an idle strategy for the snapshot load process
     * @param idleStrategy the idle strategy to use
     */
    void setIdleStrategy(final IdleStrategy idleStrategy);

    /**
     * Called by the clustered service once a snapshot needs to be taken
     * @param snapshotPublication the publication to write snapshot data to
     */
    void takeSnapshot(final ExclusivePublication snapshotPublication);

    /**
     * Called by the clustered service once a snapshot has been provided by the cluster
     * @param snapshotImage the image to read snapshot data from
     */
    void loadSnapshot(final Image snapshotImage);
}
