package gc.garcol.bankapp.service;

import gc.garcol.protocol.CreateAccountResultDecoder;
import gc.garcol.protocol.DepositAccountResultDecoder;
import gc.garcol.protocol.TransferAccountResultDecoder;
import gc.garcol.protocol.WithdrawAccountResultDecoder;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.cluster.codecs.MessageHeaderDecoder;
import io.aeron.logbuffer.Header;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;

@Slf4j
public abstract class AccountClusterEgressListenerAbstract implements AccountClusterEgressListener {

    private final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    @Override
    public void onMessage(
        long clusterSessionId,
        long timestamp,
        DirectBuffer buffer,
        int offset,
        int length,
        Header header
    ) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            log.error("Message too short: length={}", length);
            return;
        }
        messageHeaderDecoder.wrap(buffer, offset);

        log.debug("[AccountEgress] onMessage: templateId={}", messageHeaderDecoder.templateId());
        switch (messageHeaderDecoder.templateId()) {
            case CreateAccountResultDecoder.TEMPLATE_ID -> processCreateAccount(buffer, offset);
            case WithdrawAccountResultDecoder.TEMPLATE_ID -> processWithdrawAccount(buffer, offset);
            case DepositAccountResultDecoder.TEMPLATE_ID -> processDepositAccount(buffer, offset);
            case TransferAccountResultDecoder.TEMPLATE_ID -> processTransferAccount(buffer, offset);
            default -> {
            }
        }
    }

    @Override
    public void onSessionEvent(
        long correlationId,
        long clusterSessionId,
        long leadershipTermId,
        int leaderMemberId,
        EventCode code,
        String detail
    ) {
        if (code != EventCode.OK) {
            log.info("onSessionEvent: code={}, detail={}", code, detail);
        }
    }

    @Override
    public void onNewLeader(
        long clusterSessionId,
        long leadershipTermId,
        int leaderMemberId,
        String ingressEndpoints
    ) {
        log.info("onNewLeader: leadershipTermId={}, leaderMemberId={}, ingressEndpoints={}",
            leadershipTermId, leaderMemberId, ingressEndpoints);
    }
}
