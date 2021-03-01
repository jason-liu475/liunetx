package org.liu.liunetx.common.api;

import org.liu.liunetx.common.exception.LiuNetxException;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;

public interface Processor {
    default LiuNetxMessageType getType() {
        return LiuNetxMessageType.DEFAULT;
    }
    default void processData(LiuNetxMessage message) throws LiuNetxException{
        throw new LiuNetxException("Unknown type:" + message.getType());
    }
}
