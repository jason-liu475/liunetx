package org.liu.liunetx.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.liu.liunetx.common.codec.LiuNetxMessageDecode;
import org.liu.liunetx.common.codec.LiuNetxMessageEncode;
import org.liu.liunetx.handler.LiuNetxClientHandler;
import org.liu.liunetx.net.TcpConnection;
@Slf4j
public class LiuNetxClient {
    public void connect(String serverHost,int serverPort,String password,int remotePort,String proxyAddress,int proxyPort) throws Exception{
        TcpConnection connection = new TcpConnection();
        ChannelFuture future = connection.connect(serverHost, serverPort, new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel socketChannel) throws Exception {
                LiuNetxClientHandler clientHandler = new LiuNetxClientHandler(serverPort,password,proxyAddress,proxyPort);
                socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4),
                        new LiuNetxMessageDecode(),new LiuNetxMessageEncode(),
                        new IdleStateHandler(60,30,0),clientHandler);
            }
        });
        future.addListener(f -> new Thread(() -> {
            while (true){
                try {
                    connect(serverHost,serverPort,password,remotePort,proxyAddress,proxyPort);
                    break;
                }catch (Exception e){
                    log.error("error!",e);
                    try {
                        Thread.sleep(10000);
                    }catch (InterruptedException ex){
                        log.error("error!",ex);
                    }
                }
            }
        }).start());
    }
}
