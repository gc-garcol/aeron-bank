package gc.garcol.bankapp.app;

import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

@Configuration
public class AccountConfiguration {

    @Bean
    public IdleStrategy commandIdleStrategy() {
        return new SleepingMillisIdleStrategy();
    }

    @Bean
    public OneToOneRingBuffer commandBuffer() {
        final var adminClusterBuffer =
            new UnsafeBuffer(BufferUtil.allocateDirectAligned((1 << 13) + TRAILER_LENGTH, 8));
        return new OneToOneRingBuffer(adminClusterBuffer);
    }
}
