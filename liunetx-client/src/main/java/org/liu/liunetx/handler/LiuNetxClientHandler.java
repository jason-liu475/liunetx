package org.liu.liunetx.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Data;
import org.liu.liunetx.api.*;
import org.liu.liunetx.common.exception.LiuNetxException;
import org.liu.liunetx.common.handler.LiuNetxCommonHandler;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public class LiuNetxClientHandler extends LiuNetxCommonHandler {
    Log log = LogFactory.get();
    private int port;
    private String password;
    private String proxyAddress;
    private int proxyPort;

    public LiuNetxClientHandler(int port,String password,String proxyAddress,int proxyPort){
        this.port = port;
        this.password = password;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }

    private Map<String,LiuNetxCommonHandler> channelHandlerMap = new ConcurrentHashMap<>();
    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<LiuNetxMessageType, ClientProcessor> processorMap = new HashMap<>();
    static {
        processorMap.put(LiuNetxMessageType.REGISTER_RESULT,new RegisterProcessor());
        processorMap.put(LiuNetxMessageType.CONNECTED,new ConnectedProcessor());
        processorMap.put(LiuNetxMessageType.DISCONNECTED,new DisconnectedProcessor());
        processorMap.put(LiuNetxMessageType.DATA,new DataProcessor());
        processorMap.put(LiuNetxMessageType.KEEPALIVE,new KeepAliveProcessor());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        LiuNetxMessage message = new LiuNetxMessage();
        message.setType(LiuNetxMessageType.REGISTER);
        JSONObject metaData = new JSONObject();
        metaData.put("port",this.port);
        metaData.put("password",this.password);
        message.setMetaData(metaData);
        ctx.writeAndFlush(message);
        super.channelActive(ctx);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{
        LiuNetxMessage message = (LiuNetxMessage)msg;
        if(processorMap.containsKey(message.getType())){
            processorMap.get(message.getType()).processData(message,this);
        }else{
            throw new LiuNetxException("Unknown type:" + message.getType());
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        channelGroup.close();
        log.error("Loss connection to liunetx server, Please restart!");
    }
}
