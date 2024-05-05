package gc.garcol.cluster.infra.adapter.domain;

import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.common.core.ClientSessions;
import io.aeron.cluster.service.ClientSession;
import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public abstract class AbstractSessionMessageContext implements SessionMessageContext {

  protected abstract ClientSession getSession();

  protected abstract ClientSessions getClientSessions();

  @Override
  public void reply(DirectBuffer buffer, int offset, int length) {
    offerToSession(getSession(), buffer, offset, length);
  }

  @Override
  public void broadcast(DirectBuffer buffer, int offset, int length) {
    getClientSessions().getAllSessions().forEach(clientSession -> offerToSession(clientSession,
        buffer, offset, length));
  }

  protected abstract void offerToSession(
      final ClientSession targetSession,
      final DirectBuffer buffer,
      final int offset,
      final int length);
}
