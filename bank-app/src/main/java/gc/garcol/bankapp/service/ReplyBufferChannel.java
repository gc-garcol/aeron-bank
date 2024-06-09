package gc.garcol.bankapp.service;

import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface ReplyBufferChannel {
    void setReplyBuffer(ManyToOneRingBuffer commandBuffer);
}
