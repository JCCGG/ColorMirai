package coloryr.colormirai.plugin.socket.pack.to;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/**
 * 16 [机器人]主动或被动重新登录（事件）
 */
public class BotReloginEventPack extends PackBase {
    /**
     * 原因消息
     */
    public String message;

    public BotReloginEventPack(long qq, String message) {
        this.message = message;
        this.qq = qq;
    }
}
