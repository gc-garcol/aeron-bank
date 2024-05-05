package gc.garcol.cluster.infra;

import io.aeron.cluster.service.ClientSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thaivc
 * @since 2024
 */
public class ClientSessionsImpl implements ClientSessions {

  private final List<ClientSession> allSessions = new ArrayList<>();

  @Override
  public void addSession(ClientSession session) {
    allSessions.add(session);
  }

  @Override
  public void removeSession(ClientSession session) {
    allSessions.remove(session);
  }

  @Override
  public List<ClientSession> getAllSessions() {
    return allSessions;
  }
}
