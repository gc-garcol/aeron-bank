package gc.garcol.bankapp.service;

import gc.garcol.bankapp.service.constants.ConnectionState;
import gc.garcol.protocol.ConnectClusterDecoder;
import gc.garcol.protocol.DisconnectClusterDecoder;
import gc.garcol.protocol.MessageHeaderDecoder;
import io.aeron.cluster.client.AeronCluster;
import io.aeron.cluster.client.EgressListener;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.aeron.samples.cluster.ClusterConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.agrona.MutableDirectBuffer;

@Slf4j
public abstract class SystemCommandHandlerAbstract implements SystemCommandHandler {
    protected ConnectionState connectionState = ConnectionState.NOT_CONNECTED;
    protected final MessageHeaderDecoder messageHeaderDecoder = new MessageHeaderDecoder();

    @Setter
    protected EgressListener egressListener;
    protected AeronCluster aeronCluster;
    protected MediaDriver mediaDriver;

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

    @Override
    public void tryConnectToCluster(String clusterHosts, int clusterPort, String serverHost,
        int serverPort) {

    }

    private void connectCluster(
        final int basePort,
        final int port,
        final String clusterHosts,
        final String localHostName)
    {
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

        log.info("Connected to cluster leader, node {}",aeronCluster.leaderMemberId());
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
