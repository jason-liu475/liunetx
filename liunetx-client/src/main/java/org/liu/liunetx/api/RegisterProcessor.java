package org.liu.liunetx.api;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.liu.liunetx.common.protocol.LiuNetxMessage;
import org.liu.liunetx.common.protocol.LiuNetxMessageType;
import org.liu.liunetx.handler.LiuNetxClientHandler;

public class RegisterProcessor implements ClientProcessor {
    Log log = LogFactory.get();
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
