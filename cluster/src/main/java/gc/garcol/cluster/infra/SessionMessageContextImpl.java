package gc.garcol.cluster.infra;

import gc.garcol.cluster.domain.SessionMessageContext;
import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public class SessionMessageContextImpl implements SessionMessageContext {

  @Override
  public long getClusterTime() {
    return 0;
  }

  @Override
  public void reply(DirectBuffer buffer, int offset, int length) {

  }

  @Override
  public void broadcast(DirectBuffer buffer, int offset, int length) {

  }
}
