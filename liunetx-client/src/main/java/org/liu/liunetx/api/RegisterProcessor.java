package org.liu.liunetx.api;

import lombok.extern.slf4j.Slf4j;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;
@Slf4j
public class RegisterProcessor implements ClientProcessor {
    @Override
    public LiuNetxMessageType getType() {
        return LiuNetxMessageType.REGISTER;
    }

    @Override
    public void processData(LiuNetxMessage message, LiuNetxClientHandler handler) {
        if(message.getMetaData().getBool("success")){
            log.info("register to liunetx server");
        }else{
            log.error("register fail:{}",message.getMetaData().get("reason"));
            handler.getCtx().close();
        }
    }
}
