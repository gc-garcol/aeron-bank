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
    public Account(long id) {
        this.id = id;
        this.active = true;
        this.amount = 0;
    }

    public void increase(long amount) {
        this.amount += amount;
    }

    public void decrease(long amount) {
        this.amount -= amount;
    }
}
