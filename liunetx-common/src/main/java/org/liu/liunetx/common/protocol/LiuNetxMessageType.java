package org.liu.liunetx.common.protocol;

import lombok.AllArgsConstructor;
import org.liu.liunetx.common.exception.LiuNetxException;


/**
 * @author liu
 */
@AllArgsConstructor
public enum LiuNetxMessageType {
    REGISTER(1),
    REGISTER_RESULT(2),
    CONNECTED(3),
    DISCONNECTED(4),
    DATA(5),
    KEEPALIVE(6);

    private int code;
    public int getCode() {
        return code;
    }
    public static LiuNetxMessageType valueOf(int code) throws LiuNetxException {
        for (LiuNetxMessageType item : LiuNetxMessageType.values()) {
            if (item.code == code) {
                return item;
            }
        }
        throw new LiuNetxException("LiuNetxMessageTypeEnum code error: " + code);
    }
}
