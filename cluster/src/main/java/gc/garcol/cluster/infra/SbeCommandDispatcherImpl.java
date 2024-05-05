package gc.garcol.cluster.infra;

import gc.garcol.common.core.CommandHandlerMethod;
import gc.garcol.common.core.SbeCommandDispatcher;
import gc.garcol.protocol.MessageHeaderDecoder;
import lombok.RequiredArgsConstructor;
import org.agrona.DirectBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author thaivc
 * @since 2024
 */
@RequiredArgsConstructor
public class SbeCommandDispatcherImpl implements SbeCommandDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SbeCommandDispatcherImpl.class);

    private CommandHandlerMethod[] handlers = new CommandHandlerMethod[1 << 5];

    // Inject dependencies
    private final MessageHeaderDecoder headerDecoder;

    @Override
    public void registerHandler(int commandId, CommandHandlerMethod handler) {
        int hashId = commandId / 100;
        if (hashId >= handlers.length) {
            LOGGER.info("CommandId large, extends handlers array.");
            var newHandlers = new CommandHandlerMethod[Math.max(hashId, handlers.length + handlers.length >> 1)];
            System.arraycopy(handlers, 0, newHandlers, 0, handlers.length);
            handlers = newHandlers;
        }
        handlers[hashId] = handler;
    }

    @Override
    public void dispatch(DirectBuffer buffer, int offset, int length) {
        if (length < MessageHeaderDecoder.ENCODED_LENGTH) {
            LOGGER.error("Message too short, ignored.");
            return;
        }
        headerDecoder.wrap(buffer, offset);
        if (handlers[headerDecoder.templateId()] != null) {
            handlers[headerDecoder.templateId()].handle(buffer, offset);
        } else {
            LOGGER.error("No handler for templateId: {}", headerDecoder.templateId());
        }
    }
}
