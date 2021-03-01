package org.liu.liunetx.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;

public class TcpServer {
    private Channel channel;

    @SneakyThrows
    public synchronized void bind(int port, ChannelInitializer<SocketChannel> channelInitializer){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            channel = serverBootstrap.bind(port).sync().channel();
        }catch (Exception e){
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
            throw e;
        }
    }
    public synchronized void close(){
        if(channel != null){
            channel.close();
        }
    }
}
