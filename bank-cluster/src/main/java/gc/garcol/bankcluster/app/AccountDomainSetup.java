package gc.garcol.bankcluster.app;

import gc.garcol.bankcluster.domain.TimerManager;
import gc.garcol.bankcluster.domain.account.AccountClusterClientResponder;
import gc.garcol.bankcluster.domain.account.Accounts;
import gc.garcol.bankcluster.infra.adapter.domain.AccountClusterClientResponderAdapter;
import gc.garcol.bankcluster.infra.adapter.domain.SessionMessageContextAdapter;
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
