package gc.garcol.cluster.app;

import gc.garcol.protocol.MessageHeaderDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author thaivc
 * @since 2024
 */
@Configuration
public class CommonDecoderConfiguration {
    @Bean
    public MessageHeaderDecoder messageHeaderDecoder() {
        return new MessageHeaderDecoder();
    }
}
