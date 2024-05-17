package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.adapter.domain.AccountClusterClientResponderAdapter;
import gc.garcol.cluster.infra.adapter.domain.SessionMessageContextAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountDomainSetup {
    @Bean
    public AccountClusterClientResponder clusterClientResponder(final SessionMessageContextAdapter context) {
        return new AccountClusterClientResponderAdapter(context);
    }

    @Bean
    public Accounts accounts(
        final SessionMessageContextAdapter context,
        final AccountClusterClientResponder accountClusterClientResponder,
        final TimerManager timerManager
    ) {
        return new Accounts(context, accountClusterClientResponder, timerManager);
    }
}
