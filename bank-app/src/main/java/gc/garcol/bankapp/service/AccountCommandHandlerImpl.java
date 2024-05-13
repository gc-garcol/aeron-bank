package gc.garcol.bankapp.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;

/**
 * @author thaivc
 * @since 2024
 */
@Getter
@RequiredArgsConstructor
public class AccountCommandHandlerImpl extends AccountCommandHandlerAbstract implements CommandBufferChannel {
    private final OneToOneRingBuffer commandBuffer;
}
