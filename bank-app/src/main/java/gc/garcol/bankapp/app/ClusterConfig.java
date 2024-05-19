package gc.garcol.bankapp.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "cluster")
public class ClusterConfig {

    public String responseHost;

    public int responsePort;

    public String clusterHosts;

    public int clusterPort;
}
