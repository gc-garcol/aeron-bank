package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.account.Accounts;
import io.aeron.Image;
import io.aeron.logbuffer.Header;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class SnapshotManagerImpl implements SnapshotManager {

    // Inject session
    private final Accounts auctions;
    private final SessionMessageContext context;

    @Setter
    private IdleStrategy idleStrategy;
    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
    }

    @Override
    public void loadSnapshot(Image snapshotImage) {

    }
}
