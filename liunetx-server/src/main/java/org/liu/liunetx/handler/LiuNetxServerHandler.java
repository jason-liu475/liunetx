package org.liu.liunetx.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.liu.liunetx.common.exception.LiuNetxException;
import org.liu.liunetx.common.handler.LiuNetxCommonHandler;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.net.TcpServer;

import java.util.Objects;


public class LiuNetxServerHandler extends LiuNetxCommonHandler {
    Log log = LogFactory.get();
    private TcpServer remoteConnectionServer = new TcpServer();

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private String password;
    private int port;

    private boolean registered = false;

    public LiuNetxServerHandler(String password){
        this.password = password;
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LiuNetxMessage message = (LiuNetxMessage)msg;
        if(message.getType() == LiuNetxMessageType.REGISTER){
            processRegister(message);
        }else if(registered){
            if(message.getType() == LiuNetxMessageType.DISCONNECTED){
                processDisconnected(message);
            }else if(message.getType() == LiuNetxMessageType.DATA){
                processData(message);
            }else if(message.getType() == LiuNetxMessageType.KEEPALIVE){
                log.info("heartbeat request,channelId:{}",message.getMetaData().getStr("channelId"));
            }else{
                throw new LiuNetxException("Unknown type:" + message.getType());
            }
        }else{
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        remoteConnectionServer.close();
        if(registered){
            log.info("Stop server in port:{}",port);
        }
    }

    private void processRegister(LiuNetxMessage message) {
        JSONObject resultObject = new JSONObject();

        String password = message.getMetaData().get("password").toString();
        if(this.password != null && !Objects.equals(this.password,password)){
            resultObject.put("success",false);
            resultObject.put("reason","password is wrong.");
        }else{
            int port = message.getMetaData().getInt("port");
            try{
                LiuNetxServerHandler thisHandler = this;
                remoteConnectionServer.bind(port,new ChannelInitializer<SocketChannel>(){
                    @Override
                    public void initChannel(SocketChannel socketChannel) throws Exception{
                        socketChannel.pipeline().addLast(new ByteArrayDecoder(),new ByteArrayEncoder(),new RemoteProxyHandler(thisHandler));
                        channels.add(socketChannel);
                    }
                });
                resultObject.put("success",true);
                this.port = port;
                registered = true;
                log.info("Register success,start server on port:{}",port);
            }catch (Exception e){
                resultObject.put("success",false);
                resultObject.put("reason",e.getMessage());
                log.error("some exception occur",e);
            }
        }
        LiuNetxMessage response = new LiuNetxMessage();
        response.setType(LiuNetxMessageType.REGISTER_RESULT);
        response.setMetaData(resultObject);
        ctx.writeAndFlush(response);
        if(!registered){
            log.error("Client register error:{}",resultObject.get("reason"));
            ctx.close();
        }
    }

    private void processData(LiuNetxMessage message) {
        channels.writeAndFlush(message.getData(),channel -> Objects.equals(channel.id().asLongText(),message.getMetaData().get("channelId")));
    }

    private void processDisconnected(LiuNetxMessage message) {
        channels.close(channel -> Objects.equals(channel.id().asLongText(),message.getMetaData().get("channelId")));
    }
}
