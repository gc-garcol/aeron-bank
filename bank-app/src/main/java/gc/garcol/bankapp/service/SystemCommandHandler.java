package gc.garcol.bankapp.service;

import org.agrona.MutableDirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface SystemCommandHandler {
    void processConnectCluster(final MutableDirectBuffer buffer, final int offset);

    void processDisconnectCluster(final MutableDirectBuffer buffer, final int offset);

    void tryConnectToCluster();
}
