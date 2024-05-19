package gc.garcol.bankcluster.infra;

import gc.garcol.bankcluster.domain.account.Accounts;
import gc.garcol.protocol.*;
import io.aeron.ExclusivePublication;
import io.aeron.Image;
import io.aeron.Publication;
import io.aeron.logbuffer.Header;
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
public class AccountSnapshotManagerImpl extends AccountSnapshotManagerAbstract {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountSnapshotManagerImpl.class);
    private static final int RETRY_COUNT = 3;

    private final Accounts accounts;

    @Setter
    private IdleStrategy idleStrategy;
    private boolean snapshotFullyLoaded = false;

    @Override
    public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            return;
        }
        headerDecoder.wrap(buffer, offset);

        switch (headerDecoder.templateId()) {
            case AccountIdSnapshotDecoder.TEMPLATE_ID -> onAccountIdGeneratorSnapshot(buffer, offset, length);
            case AccountSnapshotDecoder.TEMPLATE_ID -> onAccountSnapshot(buffer, offset, length);
            case EndOfSnapshotDecoder.TEMPLATE_ID -> onEndOfSnapshot(buffer, offset, length);
            default -> LOGGER.warn("Unknown template id: {}", headerDecoder.templateId());
        }
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

    @Override
    public void onAccountSnapshot(final DirectBuffer buffer, final int offset, final int length) {
        accountSnapshotDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        final long id = accountSnapshotDecoder.id();
        final long amount = accountSnapshotDecoder.amount();
        final boolean active = accountSnapshotDecoder.active() == BooleanType.TRUE;
        accounts.restoreAccount(id, amount, active);
    }

    @Override
    public void onAccountIdGeneratorSnapshot(final DirectBuffer buffer, final int offset, final int length) {
        accountIdSnapshotDecoder.wrapAndApplyHeader(buffer, offset, headerDecoder);
        final long lastId = accountIdSnapshotDecoder.lastId();
        accounts.restoreAutoIdGenerator(lastId);
    }

    @Override
    public void onEndOfSnapshot(final DirectBuffer buffer, final int offset, final int length) {
        snapshotFullyLoaded = true;
    }

}
