package org.liu.liunetx.api;

import lombok.extern.slf4j.Slf4j;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;
@Slf4j
public class KeepAliveProcessor implements ClientProcessor {
    @Override
    public LiuNetxMessageType getType() {
        return LiuNetxMessageType.KEEPALIVE;
    }

    @Override
    public void processData(LiuNetxMessage message, LiuNetxClientHandler handler) {
        log.info("heartbeat request,channelId:{}",message.getMetaData().getStr("channelId"));
    }
}
