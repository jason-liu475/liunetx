package org.liu.liunetx.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.liu.liunetx.common.protocol.LiuNetxMessage;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class LiuNetxMessageEncode extends MessageToByteEncoder<LiuNetxMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, LiuNetxMessage msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)){
            dataOutputStream.writeInt(msg.getType().getCode());
            byte[] metaDataBytes = msg.getMetaData().toString().getBytes(CharsetUtil.UTF_8);
            dataOutputStream.writeInt(metaDataBytes.length);
            dataOutputStream.write(metaDataBytes);
            if(msg.getData() != null && msg.getData().length > 0){
                dataOutputStream.write(msg.getData());
            }
            byte[] data = byteArrayOutputStream.toByteArray();
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
