package org.liu.liunetx.api;

import org.liu.liunetx.common.api.Processor;
import org.liu.liunetx.common.exception.LiuNetxException;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.handler.LiuNetxClientHandler;

/**
 * @author liu
 */
public interface ClientProcessor extends Processor {
    default void processData(LiuNetxMessage message, LiuNetxClientHandler handler) throws Exception {
        throw new LiuNetxException("Unknown type:" + message.getType());
    }
}
