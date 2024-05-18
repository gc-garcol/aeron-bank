package gc.garcol.cluster.app;

import gc.garcol.cluster.domain.TimerManager;
import gc.garcol.cluster.domain.account.AccountClusterClientResponder;
import gc.garcol.cluster.domain.account.Accounts;
import gc.garcol.cluster.infra.AppClusteredService;
import gc.garcol.cluster.infra.ClientSessionsImpl;
import gc.garcol.cluster.infra.AccountSnapshotManagerImpl;
import gc.garcol.cluster.infra.adapter.domain.AccountClusterClientResponderAdapter;
import gc.garcol.cluster.infra.adapter.domain.SessionMessageContextAdapter;
import gc.garcol.cluster.infra.adapter.domain.TimerManagerAdapter;
import gc.garcol.common.core.ClientSessions;
import gc.garcol.common.core.SbeCommandDispatcher;
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
    public TimerManager timerManager(final SessionMessageContextAdapter context) {
        return new TimerManagerAdapter(context);
    }

    @Bean
    public SnapshotManager snapshotManager(final Accounts accounts, final SessionMessageContextAdapter context) {
        return new AccountSnapshotManagerImpl(accounts, context);
    }

    @Bean
    public AppClusteredService appClusteredService(
        final ClientSessions clientSessions,
        final SessionMessageContextAdapter context,
        final AccountClusterClientResponder accountClusterClientResponder,
        final TimerManager timerManager,
        final Accounts accounts,
        final SnapshotManager snapshotManager,
        final SbeCommandDispatcher sbeCommandDispatcher
    ) {
        return new AppClusteredService(
            clientSessions,
            context,
            accountClusterClientResponder,
            timerManager,
            accounts,
            snapshotManager,
            sbeCommandDispatcher
        );
    }
}
