package gc.garcol.bankapp.service;

import gc.garcol.bankapp.service.constants.ConnectionState;
import gc.garcol.common.exception.Bank5xxException;
import gc.garcol.protocol.ConnectClusterDecoder;
import gc.garcol.protocol.DisconnectClusterDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
import gc.garcol.protocol.MessageHeaderEncoder;
import io.aeron.Publication;
import io.aeron.cluster.client.AeronCluster;
import io.aeron.cluster.client.EgressListener;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.aeron.samples.cluster.ClusterConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class SystemCommandHandlerAbstract implements SystemCommandHandler {
    private static final int RETRY_COUNT = 10;
    protected ConnectionState connectionState = ConnectionState.NOT_CONNECTED;

    protected final MessageHeaderEncoder messageHeaderEncoder = new MessageHeaderEncoder();
    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    @Setter
    protected EgressListener egressListener;
    protected AeronCluster aeronCluster;
    protected MediaDriver mediaDriver;

    protected IdleStrategy[] idleStrategy = new IdleStrategy[RETRY_COUNT];

    {
        for (int i = 0; i < RETRY_COUNT; i++) {
            idleStrategy[i] = new SleepingMillisIdleStrategy((long) Math.min(Math.pow(100, i), 5_000));
        }
    }

    // messages to commandBuffer, sent by system
    protected final ConnectClusterDecoder connectClusterDecoder = new ConnectClusterDecoder();
    protected final DisconnectClusterDecoder disconnectClusterDecoder = new DisconnectClusterDecoder();

    @Override
    public void processConnectCluster(MutableDirectBuffer buffer, int offset) {
        Objects.requireNonNull(egressListener, "Egress listener is not set");

        connectClusterDecoder.wrapAndApplyHeader(buffer, offset, messageHeaderDecoder);
        connectCluster(connectClusterDecoder.baseport(), connectClusterDecoder.port(),
            connectClusterDecoder.clusterHosts(), connectClusterDecoder.localhostName());
        connectionState = ConnectionState.CONNECTED;
    }

    @Override
    public void processDisconnectCluster(MutableDirectBuffer buffer, int offset) {
        log.info("Disconnecting from cluster");
        disconnectCluster();
        connectionState = ConnectionState.NOT_CONNECTED;
        log.info("Cluster disconnected");
    }

    protected void publishCommandToCluster(final DirectBuffer buffer, final int offset, final int length) {
        if (!ConnectionState.CONNECTED.equals(connectionState)) {
            throw new Bank5xxException("Require connect to cluster first");
        }
        int retries = 0;
        while (retries < RETRY_COUNT) {
            final long result = aeronCluster.offer(buffer, offset, length);
            if (result > 0L) return;
            final int resultInt = (int) result;
            switch (resultInt) {
                case (int) Publication.ADMIN_ACTION -> log.warn("Admin action on cluster offer");
                case (int) Publication.BACK_PRESSURED -> log.warn("Backpressure on cluster offer");
                case (int) Publication.NOT_CONNECTED -> log.warn("Cluster is not connected. Message losts.");
                case (int) Publication.MAX_POSITION_EXCEEDED ->
                    log.warn("Maximum position has been exceeded. Message lost.");
                case (int) Publication.CLOSED -> log.error("Cluster connection is closed");
            }
            idleStrategy[retries++].idle();
            log.warn("Failed to send message to cluster. Retrying ( {} of {} )", retries, RETRY_COUNT);
        }
        log.error("Failed to send message to cluster. Message lost.");

    }

    @Override
    public void tryConnectToCluster(String clusterHosts, int clusterPort, String serverHost,
                                    int serverPort) {
        if (connectionState == ConnectionState.CONNECTED) {
            log.info("Already connected to cluster");
            return;
        }
        connectCluster(clusterPort, serverPort, clusterHosts, serverHost);
        connectionState = ConnectionState.CONNECTED;
    }

    private void connectCluster(
        final int basePort,
        final int port,
        final String clusterHosts,
        final String localHostName) {
        final List<String> hostnames = Arrays.asList(clusterHosts.split(","));
        final String ingressEndpoints = ClusterConfig.ingressEndpoints(
            hostnames, basePort, ClusterConfig.CLIENT_FACING_PORT_OFFSET);
        final String egressChannel = "aeron:udp?endpoint=" + localHostName + ":" + port;
        mediaDriver = MediaDriver.launch(new MediaDriver.Context()
            .threadingMode(ThreadingMode.SHARED)
            .dirDeleteOnStart(true)
            .errorHandler(error -> log.error("Media Driver error", error))
            .dirDeleteOnShutdown(true));
        aeronCluster = AeronCluster.connect(
            new AeronCluster.Context()
                .egressListener(egressListener)
                .egressChannel(egressChannel)
                .ingressChannel("aeron:udp?term-length=64k")
                .ingressEndpoints(ingressEndpoints)
                .errorHandler(error -> log.error("Aeron Cluster error", error))
                .aeronDirectoryName(mediaDriver.aeronDirectoryName()));

        log.info("Connected to cluster leader, node {}", aeronCluster.leaderMemberId());
    }

    private void disconnectCluster() {
        egressListener = null;
        if (aeronCluster != null) {
            aeronCluster.close();
        }
        if (mediaDriver != null) {
            mediaDriver.close();
        }
    }
}
