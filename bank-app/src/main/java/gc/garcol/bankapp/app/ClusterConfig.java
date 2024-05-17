package gc.garcol.bankapp.app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClusterConfig {

    @Value("${server.host}")
    public String serverHost;

    @Value("${server.port}")
    public int serverPort;

    @Value("${cluster.host}")
    public String clusterHosts;

    @Value("${cluster.port}")
    public int clusterPort;
}
