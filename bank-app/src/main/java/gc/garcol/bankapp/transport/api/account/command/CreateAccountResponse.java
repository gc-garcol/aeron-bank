package gc.garcol.bankapp.transport.api.account.command;

import gc.garcol.bankapp.transport.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAccountResponse extends BaseResponse {
    private String correlationId;
    private long accountId;
}
