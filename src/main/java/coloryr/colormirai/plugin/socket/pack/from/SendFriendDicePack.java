package coloryr.colormirai.plugin.socket.pack.from;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/*
 * 96 [插件]发送朋友骰子
 */
public class SendFriendDicePack extends PackBase {
    /*
     * QQ号
     */
    public long id;
    /*
     * 点数
     */
    public int dice;
}
