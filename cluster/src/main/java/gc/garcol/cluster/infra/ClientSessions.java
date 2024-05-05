package gc.garcol.cluster.infra;

import io.aeron.cluster.service.ClientSession;
import java.util.List;

/**
 * @author thaivc
 * @since 2024
 */
public interface ClientSessions {
  /**
   * Adds a client session
   * @param session the session to add
   */
  void addSession(final ClientSession session);

  /**
   * Removes a client session
   * @param session the session to remove
   */
  void removeSession(final ClientSession session);

  /**
   * Gets all client sessions known
   * @return the list of client sessions
   */
  List<ClientSession> getAllSessions();
}
