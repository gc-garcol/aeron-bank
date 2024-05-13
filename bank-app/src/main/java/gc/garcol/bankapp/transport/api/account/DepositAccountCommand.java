package gc.garcol.bankapp.transport.api.account;

public record DepositAccountCommand(
    String correlationId,
    long accountId,
    long amount
) {

}
