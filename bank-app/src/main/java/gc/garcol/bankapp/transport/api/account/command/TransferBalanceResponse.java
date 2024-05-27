package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransferBalanceResponse extends BaseResponse {
    private String correlationId;
    private long fromAccountId;
    private long toAccountId;
    private long transferAmount;
    private long currentAmount;
}
