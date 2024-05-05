package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.adapter.domain.SessionMessageContextAdapter;
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

  @Override
  public void onStart(Cluster cluster, Image snapshotImage) {
    snapshotManager.setIdleStrategy(cluster.idleStrategy());
    context.setIdleStrategy(cluster.idleStrategy());
    timerManager.setCluster(cluster);
    if (snapshotImage != null)
    {
      snapshotManager.loadSnapshot(snapshotImage);
    }
  }

  @Override
  public void onSessionOpen(ClientSession session, long timestamp) {

  }

  @Override
  public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason) {

  }

  @Override
  public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer,
      int offset, int length, Header header) {

  }

  @Override
  public void onTimerEvent(long correlationId, long timestamp) {

  }

  @Override
  public void onTakeSnapshot(ExclusivePublication snapshotPublication) {

  }

  @Override
  public void onRoleChange(Role newRole) {

  }

  @Override
  public void onTerminate(Cluster cluster) {

  }
}
