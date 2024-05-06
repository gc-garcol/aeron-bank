package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.adapter.domain.SessionMessageContextAdapter;
import gc.garcol.common.core.ClientSessions;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.common.core.SnapshotManager;
import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.ClientSession;
import io.aeron.cluster.service.Cluster;
import io.aeron.cluster.service.Cluster.Role;
import io.aeron.cluster.service.ClusteredService;
import io.aeron.logbuffer.Header;
import lombok.RequiredArgsConstructor;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class AppClusteredService implements ClusteredService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppClusteredService.class);

    private final ClientSessions clientSessions;
    private final SessionMessageContextAdapter context;
    private final AccountClusterClientResponder accountClusterClientResponder;
    private final TimerManager timerManager;
    private final Accounts accounts;
    private final SnapshotManager snapshotManager;
    private final SbeCommandDispatcher sbeCommandDispatcher;

    @Override
    public void onStart(Cluster cluster, Image snapshotImage) {
        snapshotManager.setIdleStrategy(cluster.idleStrategy());
        context.setIdleStrategy(cluster.idleStrategy());
        timerManager.setCluster(cluster);
        if (snapshotImage != null) {
            snapshotManager.loadSnapshot(snapshotImage);
        }
    }

    @Override
    public void onSessionOpen(ClientSession session, long timestamp) {
        LOGGER.info("Client session opened");
        context.setClusterTime(timestamp);
        clientSessions.addSession(session);
    }

    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason) {
        context.setClusterTime(timestamp);
        clientSessions.removeSession(session);
    }

    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer,
                                 int offset, int length, Header header) {
        context.setSessionContext(session, timestamp);
        sbeCommandDispatcher.dispatch(buffer, offset, length);
    }

    @Override
    public void onTimerEvent(long correlationId, long timestamp) {
        context.setClusterTime(timestamp);
        timerManager.onTimerEvent(correlationId, timestamp);
    }

    @Override
    public void onTakeSnapshot(ExclusivePublication snapshotPublication) {
        snapshotManager.takeSnapshot(snapshotPublication);
    }

    @Override
    public void onRoleChange(Role newRole) {
        LOGGER.info("Role change: {}", newRole);
    }

    @Override
    public void onTerminate(final Cluster cluster) {
        LOGGER.info("Terminating");
    }
}
