package gc.garcol.common.core;

import org.agrona.DirectBuffer;

/**
 * @author thaivc
 * @since 2024
 */
public interface SbeCommandDispatcher {
    /**
     * Hash the commandId to find the appropriate handler.
     * @param commandId
     * @return the hash value
     */
    int hashCommand(int commandId);

    /**
     *
     * @param commandId the command TEMPLATE_ID must be unique and divisible by 100
     * @param handler
     */
    void registerHandler(int commandId, CommandHandlerMethod handler);


    /**
     * Dispatch a message to the appropriate domain handler.
     *
     * @param buffer the buffer containing the inbound message, including a header
     * @param offset the offset to apply
     * @param length the length of the message
     */
    void dispatch(final DirectBuffer buffer, final int offset, final int length);
}
