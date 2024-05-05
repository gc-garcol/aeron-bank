package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.*;
import gc.garcol.cluster.infra.adapter.domain.AccountAccountClusterClientResponderAdapter;
import gc.garcol.cluster.infra.adapter.domain.SessionMessageContextAdapter;
import gc.garcol.cluster.infra.adapter.domain.TimerManagerAdapter;
import gc.garcol.common.core.ClientSessions;
import gc.garcol.common.core.SnapshotManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thaivc
 * @since 2024
 */
@Configuration
public class AppConfiguration {

    @Bean
    public ClientSessions clientSessions() {
        return new ClientSessionsImpl();
    }

    @Bean
    public SessionMessageContextAdapter context(final ClientSessions clientSessions) {
        return new SessionMessageContextAdapter(clientSessions);
    }

    @Bean
    public AccountClusterClientResponder clusterClientResponder(final SessionMessageContextAdapter context) {
        return new AccountAccountClusterClientResponderAdapter(context);
    }

    @Bean
    public TimerManager timerManager(final SessionMessageContextAdapter context) {
        return new TimerManagerAdapter(context);
    }

    @Bean
    public Accounts accounts(
        final SessionMessageContextAdapter context,
        final AccountClusterClientResponder accountClusterClientResponder,
        final TimerManager timerManager
    ) {
        return new Accounts(context, accountClusterClientResponder, timerManager);
    }

    @Bean
    public SnapshotManager snapshotManager(final Accounts accounts, final SessionMessageContextAdapter context) {
        return new SnapshotManagerImpl(accounts, context);
    }

    @Bean
    public AppClusteredService appClusteredService(
        final ClientSessions clientSessions,
        final SessionMessageContextAdapter context,
        final AccountClusterClientResponder accountClusterClientResponder,
        final TimerManager timerManager,
        final Accounts accounts,
        final SnapshotManager snapshotManager
    ) {
        return new AppClusteredService(
            clientSessions,
            context,
            accountClusterClientResponder,
            timerManager,
            accounts,
            snapshotManager
        );
    }
}
