package org.liu.liunetx.common.codec;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;

import java.util.List;

/**
 * @author liu
 */
public class LiuNetxMessageDecode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int type = msg.readInt();
        LiuNetxMessageType liuNetxMessageType = LiuNetxMessageType.valueOf(type);

        int metaDataLength = msg.readInt();
        CharSequence metaDataString = msg.readCharSequence(metaDataLength, CharsetUtil.UTF_8);
        JSONObject jsonObject = JSONUtil.parseObj(metaDataString);
        byte[] data = null;
        if(msg.isReadable()){
            data = ByteBufUtil.getBytes(msg);
        }

        LiuNetxMessage liuNetxMessage = new LiuNetxMessage();
        liuNetxMessage.setType(liuNetxMessageType);
        liuNetxMessage.setMetaData(jsonObject);
        liuNetxMessage.setData(data);

        out.add(liuNetxMessage);
    }
}
