package gc.garcol.bankapp.app;

import gc.garcol.bankapp.service.AccountCommandHandler;
import gc.garcol.bankapp.service.AccountCommandHandlerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.IdleStrategy;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Bootstrap {

    private final AccountCommandHandler commandHandler;
    private final IdleStrategy commandIdleStrategy;
    private final AccountCommandHandlerImpl accountCommandHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        try {
            startAccountRunner();
            tryConnectCluster();
        } catch (Exception e) {
            log.error("CANNOT CONNECT TO CLUSTER", e);
            System.exit(9);
        }
    }

    private void startAccountRunner() {
        log.info("Start account runner");
        final var agentRunner = new AgentRunner(
            commandIdleStrategy,
            Throwable::printStackTrace,
            null,
            commandHandler
        );
        AgentRunner.startOnThread(agentRunner);
    }

    private void tryConnectCluster() {
        log.info("Try to connect to cluster");
        accountCommandHandler.tryConnectToCluster();
    }
}
