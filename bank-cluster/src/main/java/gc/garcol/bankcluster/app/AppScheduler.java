package gc.garcol.bankcluster.app;

import gc.garcol.common.core.ClusterToolDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class AppScheduler {

    private final ClusterToolDispatcher clusterToolDispatcher;

    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    public void snapshot() {
        clusterToolDispatcher.triggerSnapshot();
    }
}
