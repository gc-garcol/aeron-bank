package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.common.core.SnapshotManager;
import gc.garcol.protocol.*;
import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.Publication;
import io.aeron.logbuffer.Header;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.agrona.DirectBuffer;
import org.agrona.ExpandableDirectByteBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class SnapshotManagerImpl implements SnapshotManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotManagerImpl.class);
    private static final int RETRY_COUNT = 3;

    // Inject session
    private final Accounts accounts;
    private final SessionMessageContext context;

    @Setter
    private IdleStrategy idleStrategy;
    private boolean snapshotFullyLoaded = false;

    private final ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(1 << 10);
    private final MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private final MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();

    private final AccountSnapshotEncoder accountSnapshotEncoder = new AccountSnapshotEncoder();
    private final AccountSnapshotDecoder accountSnapshotDecoder = new AccountSnapshotDecoder();
    private final AccountIdSnapshotDecoder accountIdSnapshotDecoder = new AccountIdSnapshotDecoder();
    private final AccountIdSnapshotEncoder accountIdSnapshotEncoder = new AccountIdSnapshotEncoder();
    private final EndOfSnapshotEncoder endOfSnapshotEncoder = new EndOfSnapshotEncoder();

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
    }

    @Override
    public void takeSnapshot(ExclusivePublication snapshotPublication) {
        LOGGER.info("Starting snapshot...");
        offerAccounts(snapshotPublication);
        offerAccountIdGenerator(snapshotPublication);
        offerEndOfSnapshotMarker(snapshotPublication);
        LOGGER.info("Snapshot complete");
    }

    @Override
    public void loadSnapshot(Image snapshotImage) {
        LOGGER.info("Loading snapshot...");
        snapshotFullyLoaded = false;
        Objects.requireNonNull(idleStrategy, "Idle strategy must be set before loading snapshot");
        idleStrategy.reset();
        while (!snapshotImage.isEndOfStream()) {
            idleStrategy.idle(snapshotImage.poll(this, 20));
        }

        if (!snapshotFullyLoaded) {
            LOGGER.warn("Snapshot load not completed; no end of snapshot marker found");
        }
        LOGGER.info("Snapshot load complete.");
    }

    /**
     * Offers the auctions to the snapshot publication using the AccountSnapshotEncoder
     *
     * @param snapshotPublication the publication to offer the snapshot data to
     */
    private void offerAccounts(final ExclusivePublication snapshotPublication) {
        accounts.getAccounts().forEach((id, account) -> {
            accountSnapshotEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
            accountSnapshotEncoder.id(id);
            accountSnapshotEncoder.amount(account.getAmount());
            accountSnapshotEncoder.active(account.isActive() ? BooleanType.TRUE : BooleanType.FALSE);
            retryingOffer(
                snapshotPublication,
                buffer,
                headerEncoder.encodedLength() + accountSnapshotEncoder.encodedLength()
            );
        });
    }

    /**
     * Offers the auction id generator's last id to the snapshot publication using the AuctionIdSnapshotEncoder
     *
     * @param snapshotPublication the publication to offer the snapshot data to
     */
    private void offerAccountIdGenerator(final ExclusivePublication snapshotPublication) {
        accountIdSnapshotEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
        accountIdSnapshotEncoder.lastId(accounts.getIdGenerator().get());
        retryingOffer(
            snapshotPublication,
            buffer,
            headerEncoder.encodedLength() + accountIdSnapshotEncoder.encodedLength()
        );
    }

    private void offerEndOfSnapshotMarker(final ExclusivePublication snapshotPublication) {
        endOfSnapshotEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder);
        retryingOffer(snapshotPublication, buffer,
            headerEncoder.encodedLength() + endOfSnapshotEncoder.encodedLength());
    }

    /**
     * Retries the offer to the publication if it fails on back pressure or admin action.
     * Buffer is assumed to always start at offset 0
     *
     * @param publication the publication to offer data to
     * @param buffer      the buffer holding the source data
     * @param length      the length to write
     */
    private void retryingOffer(final ExclusivePublication publication, final DirectBuffer buffer, final int length) {
        final int offset = 0;
        int retries = 0;
        while (retries < RETRY_COUNT) {
            final long result = publication.offer(buffer, offset, length);
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
        LOGGER.error("failed to offer snapshot within {} retries", RETRY_COUNT);
    }

}
