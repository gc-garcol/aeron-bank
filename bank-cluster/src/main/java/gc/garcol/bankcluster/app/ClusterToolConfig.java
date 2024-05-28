package gc.garcol.bankcluster.app;

import gc.garcol.common.core.ClusterToolDispatcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClusterToolConfig {

    @Bean
    public ClusterToolDispatcher clusterToolDispatcher(
            @Value("${aeron.cluster.clusterDir}") String clusterDirPath
    ) {
        return new ClusterToolDispatcher(clusterDirPath);
    }

}
