package org.liu.liunetx.handler;

import cn.hutool.json.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.liu.liunetx.common.handler.LiuNetxCommonHandler;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;

public class LocalProxyHandler extends LiuNetxCommonHandler {
    private LiuNetxCommonHandler proxyHandler;

    private String remoteChannelId;
    public LocalProxyHandler(LiuNetxClientHandler proxyHandler,String remoteChannelId){
        this.proxyHandler = proxyHandler;
        this.remoteChannelId = remoteChannelId;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        byte[] data = (byte[])msg;
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.DATA);
        message.setData(data);
        writeAndFlushMessage(proxyHandler.getCtx(),message);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.DISCONNECTED);
        writeAndFlushMessage(proxyHandler.getCtx(),message);
    }
    private void writeAndFlushMessage(ChannelHandlerContext ctx,LiuNetxMessage message){
        JSONObject metaData = new JSONObject();
        metaData.put("channelId",remoteChannelId);
        message.setMetaData(metaData);
        ctx.writeAndFlush(message);
    }
}
