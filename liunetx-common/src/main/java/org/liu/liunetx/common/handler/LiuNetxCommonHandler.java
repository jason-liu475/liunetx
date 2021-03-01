package org.liu.liunetx.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;


/**
 * @author liu
 */
@Slf4j
public class LiuNetxCommonHandler extends ChannelInboundHandlerAdapter {
    protected ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx(){
        return this.ctx;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        log.error("some accompanying message", cause);
        cause.printStackTrace();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent)evt;
            if(e.state() == IdleState.READER_IDLE){
                log.info("Read idle loss connection.");
                ctx.close();
            }else if(e.state() == IdleState.WRITER_IDLE){
                LiuNetxMessage liuNetxMessage = new LiuNetxMessage();
                liuNetxMessage.setType(LiuNetxMessageType.KEEPALIVE);
                ctx.writeAndFlush(liuNetxMessage);
            }
        }
    }
}
