package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.common.core.ClientSessions;
import io.aeron.Publication;
import io.aeron.cluster.service.ClientSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class SessionMessageContextAdapter extends AbstractSessionMessageContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionMessageContextAdapter.class);
    private static final long RETRY_COUNT = 3;

    @Getter
    private final ClientSessions clientSessions;

    @Setter
    private IdleStrategy idleStrategy;

    @Getter
    @Setter
    private long clusterTime;

    @Getter
    @Setter
    private ClientSession session;

    private long timestamp;

    @Override
    protected void offerToSession(ClientSession targetSession, DirectBuffer buffer, int offset, int length) {
        Objects.requireNonNull(idleStrategy, "idleStrategy must be set");
        int retries = 0;
        while (retries < RETRY_COUNT) {
            final long result = targetSession.offer(buffer, offset, length);
            if (result > 0L) {
                return;
            } else if (result == Publication.ADMIN_ACTION || result == Publication.BACK_PRESSURED) {
                LOGGER.warn("backpressure or admin action on snapshot");
            } else if (result == Publication.NOT_CONNECTED || result == Publication.MAX_POSITION_EXCEEDED) {
                LOGGER.error("unexpected publication state on snapshot: {}", result);
                return;
            }
            idleStrategy.idle();
            retries += 1;
        }
        LOGGER.error("failed to offer snapshot within {} retries. Closing client session.", RETRY_COUNT);
        session.close();
    }

    /**
     * Sets the session context for this cluster message
     *
     * @param session   the session
     * @param timestamp the timestamp
     */
    public void setSessionContext(final ClientSession session, final long timestamp) {
        this.timestamp = timestamp;
        this.session = session;
    }

    /**
     * Sets the cluster timestamp for the current context
     *
     * @param timestamp
     */
    public void setClusterTime(final long timestamp) {
        this.timestamp = timestamp;
    }
}
