package org.liu.liunetx.api;

import org.liu.liunetx.common.handler.LiuNetxCommonHandler;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;

import java.util.Objects;

public class DisconnectedProcessor implements ClientProcessor {
    @Override
    public LiuNetxMessageType getType() {
        return LiuNetxMessageType.DISCONNECTED;
    }

    @Override
    public void processData(LiuNetxMessage message, LiuNetxClientHandler handler) {
        String channelId = message.getMetaData().getStr("channelId");
        LiuNetxCommonHandler channelHandler = handler.getChannelHandlerMap().get(channelId);
        if(Objects.nonNull(channelHandler)){
            channelHandler.getCtx().close();
            handler.getChannelHandlerMap().remove(channelId);
        }
    }
}
