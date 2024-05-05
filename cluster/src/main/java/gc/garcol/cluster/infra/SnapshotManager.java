package gc.garcol.cluster.infra;

import io.aeron.Image;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.concurrent.IdleStrategy;

/**
 * @author thaivc
 * @since 2024
 */
public interface SnapshotManager extends FragmentHandler {
    void setIdleStrategy(final IdleStrategy idleStrategy);

    void loadSnapshot(final Image snapshotImage);
}
