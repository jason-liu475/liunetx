package org.liu.liunetx.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.liu.liunetx.common.codec.LiuNetxMessageDecode;
import org.liu.liunetx.common.codec.LiuNetxMessageEncode;
import org.liu.liunetx.handler.LiuNetxServerHandler;
import org.liu.liunetx.net.TcpServer;

public class LiuNetxServer {
    public void start(int port,String password){
        TcpServer clientServer = new TcpServer();
        clientServer.bind(port,new ChannelInitializer<SocketChannel>(){
            @Override
            public void initChannel(SocketChannel socketChannel){
                LiuNetxServerHandler liuNetxServerHandler = new LiuNetxServerHandler(password);
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4),
                        new LiuNetxMessageDecode(),new LiuNetxMessageEncode(),
                        new IdleStateHandler(60,30,0),liuNetxServerHandler);
            }
        });
    }
}
