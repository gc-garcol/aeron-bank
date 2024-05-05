package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.infra.ClientSessions;
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

    @Override
    protected void offerToSession(ClientSession targetSession, DirectBuffer buffer, int offset, int length) {
        Objects.requireNonNull(idleStrategy, "idleStrategy must be set");
        int retries = 0;
        while (retries < RETRY_COUNT) {
            final long result = targetSession.offer(buffer, offset, length);
            switch ((int) result) {
                case (int) Publication.ADMIN_ACTION, (int) Publication.BACK_PRESSURED -> {
                    LOGGER.warn("backpressure or admin action on session offer");
                }
                case (int) Publication.NOT_CONNECTED, (int) Publication.MAX_POSITION_EXCEEDED -> {
                    LOGGER.error("unexpected state on session offer: {}", result);
                    return;
                }
                default -> {
                    if (result > 0) {
                        return;
                    }
                }
            }
            idleStrategy.idle();
            retries += 1;
        }
        LOGGER.error("failed to offer snapshot within {} retries. Closing client session.",
            RETRY_COUNT);
        session.close();
    }
}
