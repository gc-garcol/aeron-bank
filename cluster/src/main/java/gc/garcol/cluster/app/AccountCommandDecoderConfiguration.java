package gc.garcol.cluster.app;

import gc.garcol.protocol.AddAccountCommandDecoder;
import gc.garcol.protocol.DepositAccountCommandDecoder;
import gc.garcol.protocol.WithdrawAccountCommandDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thaivc
 * @since 2024
 */
@Configuration
public class AccountCommandDecoderConfiguration {

    @Bean
    public AddAccountCommandDecoder addAccountCommandDecoder() {
        return new AddAccountCommandDecoder();
    }

    @Bean
    public DepositAccountCommandDecoder depositAccountCommandDecoder() {
        return new DepositAccountCommandDecoder();
    }

    @Bean
    public WithdrawAccountCommandDecoder withdrawAccountCommandDecoder() {
        return new WithdrawAccountCommandDecoder();
    }
}
