package gc.garcol.cluster.domain.account;

import gc.garcol.cluster.domain.ClusterClientResponder;
import gc.garcol.cluster.domain.SessionMessageContext;
import gc.garcol.cluster.domain.TimerManager;
import lombok.RequiredArgsConstructor;
import org.agrona.collections.Long2ObjectHashMap;
import org.agrona.collections.MutableLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class Accounts {

  private static final Logger LOGGER = LoggerFactory.getLogger(Accounts.class);

  private final Long2ObjectHashMap<Account> accounts = new Long2ObjectHashMap<>();
  private final MutableLong idGenerator = new MutableLong(0);

  // Injected dependencies
  private final SessionMessageContext context;
  private final ClusterClientResponder clusterClientResponder;
  private final TimerManager timerManager;
}
