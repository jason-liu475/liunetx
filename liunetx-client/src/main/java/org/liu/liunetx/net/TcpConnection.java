package org.liu.liunetx.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author liu
 */
public class TcpConnection {

    public ChannelFuture connect(String host, int port, ChannelInitializer<SocketChannel> channelInitializer) throws Exception{
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(channelInitializer);

            Channel channel = bootstrap.connect(host, port).sync().channel();
            return channel.closeFuture().addListener(future -> workGroup.shutdownGracefully());
        }catch (Exception e){
            workGroup.shutdownGracefully();
            throw e;
        }
    }
}
