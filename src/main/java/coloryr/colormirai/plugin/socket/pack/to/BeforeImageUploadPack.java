package coloryr.colormirai.plugin.socket.pack.to;

import coloryr.colormirai.plugin.socket.pack.PackBase;

/***
 * 1 [机器人]图片上传前. 可以阻止上传（事件）
 */
public class BeforeImageUploadPack extends PackBase {
    /***
     * 图片UUID
     */
    public String name;
    /***
     * 发送给的号码
     */
    public long id;

    public BeforeImageUploadPack(long qq, String name, long id) {
        this.qq = qq;
        this.id = id;
        this.name = name;
    }
}
