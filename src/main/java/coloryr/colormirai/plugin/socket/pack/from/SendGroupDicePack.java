package coloryr.colormirai.plugin.socket.pack.from;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/*
97 [插件]发送群骰子
id:群号
dice:点数
 */
public class SendGroupDicePack extends PackBase {
    public long id;
    public int dice;
}
