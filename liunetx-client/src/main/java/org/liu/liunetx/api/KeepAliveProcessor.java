package org.liu.liunetx.api;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;

public class KeepAliveProcessor implements ClientProcessor {
    Log log = LogFactory.get();
    @Override
    public LiuNetxMessageType getType() {
        return LiuNetxMessageType.KEEPALIVE;
    }

    @Override
    public void processData(LiuNetxMessage message, LiuNetxClientHandler handler) {
        log.info("heartbeat request,channelId:{}",message.getMetaData().getStr("channelId"));
    }
}
