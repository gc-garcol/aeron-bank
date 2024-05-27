package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WithdrawAccountResponse extends BaseResponse {
    private String correlationId;
    private long accountId;
    private long withdrawAmount;
    private long currentAmount;
}
