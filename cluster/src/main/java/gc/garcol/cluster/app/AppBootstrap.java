package gc.garcol.cluster.app;

import gc.garcol.cluster.infra.AppClusteredService;
import gc.garcol.common.exception.BankException;
import io.aeron.cluster.ClusteredMediaDriver;
import io.aeron.cluster.service.ClusteredServiceContainer;
import io.aeron.samples.cluster.ClusterConfig;
import lombok.Setter;
import org.agrona.concurrent.ShutdownSignalBarrier;
import org.agrona.concurrent.SystemEpochClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author thaivc
 * @since 2024
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "aeron.cluster")
public class AppBootstrap {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppBootstrap.class);

    private String hosts;
    private int node;
    private int port;
    private String baseDir;
    private boolean dnsDelay;

    @Autowired
    private AppClusteredService appClusteredService;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        LOGGER.info("Starting Cluster Node... {} {} {}", hosts, node, port);
        final ShutdownSignalBarrier barrier = new ShutdownSignalBarrier();
        final List<String> hostAddresses = List.of(hosts.split(","));
        final ClusterConfig clusterConfig = ClusterConfig.create(
            node, hostAddresses, hostAddresses, port, appClusteredService
        );
        clusterConfig.consensusModuleContext().ingressChannel("aeron:udp");
        clusterConfig.baseDir(new File(baseDir));

        //this may need tuning for your environment.
        clusterConfig.consensusModuleContext().leaderHeartbeatTimeoutNs(TimeUnit.SECONDS.toNanos(3));
        //await DNS resolution of all the hostnames
        hostAddresses.forEach(this::awaitDnsResolution);

        try (
            ClusteredMediaDriver ignored = ClusteredMediaDriver.launch(
                clusterConfig.mediaDriverContext(),
                clusterConfig.archiveContext(),
                clusterConfig.consensusModuleContext()
            );
            ClusteredServiceContainer ignored1 =ClusteredServiceContainer.launch(clusterConfig.clusteredServiceContext())
        ) {
            LOGGER.info("Started Cluster Node...");
            barrier.await();
            LOGGER.info("Exiting");
        }
    }

    /**
     * Await DNS resolution of the given host. Under Kubernetes, this can take a while.
     *
     * @param host of the node to resolve
     */
    private void awaitDnsResolution(final String host) {
        if (dnsDelay) {
            LOGGER.info("Waiting 5 seconds for DNS to be registered...");
            quietSleep(5_000);
        }

        final long endTime = SystemEpochClock.INSTANCE.time() + 60_000;
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");

        boolean resolved = false;
        while (!resolved) {
            if (SystemEpochClock.INSTANCE.time() > endTime) {
                LOGGER.error("cannot resolve name {}, exiting", host);
                System.exit(-1);
            }

            try {
                InetAddress.getByName(host);
                resolved = true;
            } catch (final UnknownHostException e) {
                LOGGER.warn("cannot yet resolve name {}, retrying in 3 seconds", host);
                quietSleep(3000);
            }
        }
    }

    /**
     * Sleeps for the given number of milliseconds, ignoring any interrupts.
     *
     * @param millis the number of milliseconds to sleep.
     */
    private static void quietSleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex) {
            LOGGER.warn("Interrupted while sleeping");
            throw new BankException("Interrupted while sleeping");
        }
    }
}
