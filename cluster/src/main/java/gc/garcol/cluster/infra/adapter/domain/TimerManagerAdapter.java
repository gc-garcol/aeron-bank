package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.TimerManager;
import io.aeron.cluster.service.Cluster;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.agrona.collections.Long2ObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class TimerManagerAdapter implements TimerManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerManagerAdapter.class);

    // inject session
    private final SessionMessageContext context;

    private final Long2ObjectHashMap<Runnable> correlationIdToRunnable = new Long2ObjectHashMap<>();
    @Setter
    private Cluster cluster;
    private long correlationId = 0;

    @Override
    public long scheduleTimer(long deadline, Runnable timerRunnable) {
        return 0;
    }

    @Override
    public void restoreTimer(long timerCorrelationId, Runnable task) {

    }

    @Override
    public void onTimerEvent(long correlationId, long timestamp) {

    }
}
