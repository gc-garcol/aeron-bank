package gc.garcol.bankapp.app;

import gc.garcol.bankapp.service.AccountCommandHandler;
import lombok.RequiredArgsConstructor;
import org.agrona.concurrent.AgentRunner;
import org.agrona.concurrent.IdleStrategy;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class Bootstrap {

    private final AccountCommandHandler commandHandler;
    private final IdleStrategy commandIdleStrategy;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
      final var agentRunner = new AgentRunner(
          commandIdleStrategy,
          Throwable::printStackTrace,
          null,
          commandHandler
      );
      AgentRunner.startOnThread(agentRunner);
    }
}
