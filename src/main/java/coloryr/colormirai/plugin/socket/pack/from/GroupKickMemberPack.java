package coloryr.colormirai.plugin.socket.pack.from;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/**
 * 64 [插件]删除群员
 */
public class GroupKickMemberPack extends PackBase {
    /**
     * 群号
     */
    public long id;
    /**
     * 群员QQ号
     */
    public long fid;
    /**
     * 黑名单
     */
    public boolean black;
}
