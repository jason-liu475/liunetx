package org.liu.liunetx.api;

import cn.hutool.json.JSONObject;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;
import org.liu.liunetx.handler.LocalProxyHandler;
import org.liu.liunetx.net.TcpConnection;

public class ConnectedProcessor implements ClientProcessor {
    @Override
    public LiuNetxMessageType getType() {
        return LiuNetxMessageType.CONNECTED;
    }

    @Override
    public void processData(LiuNetxMessage message, LiuNetxClientHandler handler) throws Exception{
        try {
            TcpConnection localConnection = new TcpConnection();
            localConnection.connect(handler.getProxyAddress(), handler.getProxyPort(), new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    LocalProxyHandler localProxyHandler = new LocalProxyHandler(handler,message.getMetaData().getStr("channelId"));
                    socketChannel.pipeline().addLast(new ByteArrayDecoder(),new ByteArrayEncoder(),localProxyHandler);
                    handler.getChannelHandlerMap().put(message.getMetaData().getStr("channelId"),localProxyHandler);
                    handler.getChannelGroup().add(socketChannel);
                }
            });
        }catch (Exception e){
            LiuNetxMessage response = new LiuNetxMessage();
            response.setType(LiuNetxMessageType.DISCONNECTED);
            JSONObject result = new JSONObject();
            result.put("channelId",message.getMetaData().getStr("channelId"));
            response.setMetaData(result);
            handler.getCtx().writeAndFlush(response);
            handler.getChannelHandlerMap().remove(message.getMetaData().getStr("channelId"));
            throw e;
        }
    }
}
