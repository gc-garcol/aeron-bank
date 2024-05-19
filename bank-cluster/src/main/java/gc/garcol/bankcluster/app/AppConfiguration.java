package gc.garcol.bankcluster.app;

import gc.garcol.bankcluster.domain.TimerManager;
import gc.garcol.bankcluster.domain.account.Accounts;
import gc.garcol.bankcluster.infra.AccountSnapshotManagerImpl;
import gc.garcol.bankcluster.infra.AppClusteredService;
import gc.garcol.bankcluster.infra.ClientSessionsImpl;
import gc.garcol.bankcluster.infra.adapter.domain.SessionMessageContextAdapter;
import gc.garcol.bankcluster.infra.adapter.domain.TimerManagerAdapter;
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
    public SnapshotManager snapshotManager(final Accounts accounts) {
        return new AccountSnapshotManagerImpl(accounts);
    }

    @Bean
    public AppClusteredService appClusteredService(
        final ClientSessions clientSessions,
        final SessionMessageContextAdapter context,
        final TimerManager timerManager,
        final SnapshotManager snapshotManager,
        final SbeCommandDispatcher sbeCommandDispatcher
    ) {
        return new AppClusteredService(
            clientSessions,
            context,
            timerManager,
            snapshotManager,
            sbeCommandDispatcher
        );
    }
}
