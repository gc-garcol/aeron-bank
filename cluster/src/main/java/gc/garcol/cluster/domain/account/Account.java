package gc.garcol.cluster.domain.account;

import lombok.Data;

/**
 * @author thaivc
 * @since 2024
 */
@Data
public class Account {

  private long id;
  private long amount;
  private boolean active;
}
