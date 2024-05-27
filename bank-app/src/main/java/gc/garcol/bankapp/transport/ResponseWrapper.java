package gc.garcol.bankapp.transport;

import lombok.Data;

@Data
public class ResponseWrapper {
    private int status;
    private BaseResponse data;
    private BaseError error;
}
