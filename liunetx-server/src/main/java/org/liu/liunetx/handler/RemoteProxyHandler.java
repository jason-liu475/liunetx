package org.liu.liunetx.handler;

import cn.hutool.json.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import org.liu.liunetx.common.handler.LiuNetxCommonHandler;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;

/**
 * @author liu
 */
public class RemoteProxyHandler extends LiuNetxCommonHandler{
    private LiuNetxCommonHandler proxyHandler;

    public RemoteProxyHandler(LiuNetxCommonHandler liuNetxCommonHandler) {
        this.proxyHandler = liuNetxCommonHandler;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.CONNECTED);
        writeAndFlushMessage(ctx,message);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.DISCONNECTED);
        writeAndFlushMessage(ctx,message);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        byte[] data = (byte[])msg;
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.DATA);
        message.setData(data);
        writeAndFlushMessage(ctx,message);
    }
    private void writeAndFlushMessage(ChannelHandlerContext ctx,LiuNetxMessage message){
        JSONObject metaData = new JSONObject();
        metaData.put("channelId",ctx.channel().id().asLongText());
        message.setMetaData(metaData);
        proxyHandler.getCtx().writeAndFlush(message);
    }
}
