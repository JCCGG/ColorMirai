package coloryr.colormirai.plugin.socket.pack.to;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/***
 * 4 [机器人]被邀请加入一个群（事件）
 */
public class BotInvitedJoinGroupRequestEventPack extends PackBase {
    /***
     * 事件ID
     */
    public long eventid;
    /***
     * 群号
     */
    public long id;
    /***
     * 邀请人QQ号
     */
    public long fid;

    public BotInvitedJoinGroupRequestEventPack(long qq, long id, long fid, long eventid) {
        this.qq = qq;
        this.fid = fid;
        this.id = id;
        this.eventid = eventid;
    }
}
