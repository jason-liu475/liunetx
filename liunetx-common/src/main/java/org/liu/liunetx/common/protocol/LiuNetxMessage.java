package org.liu.liunetx.common.protocol;

import lombok.Data;

import cn.hutool.json.JSONObject;


/**
 * @author liu
 */
@Data
public class LiuNetxMessage {

    private LiuNetxMessageType type;
    private JSONObject metaData;
    private byte[] data;

}
