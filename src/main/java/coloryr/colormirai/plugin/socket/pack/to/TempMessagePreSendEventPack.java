package coloryr.colormirai.plugin.socket.pack.to;

import coloryr.colormirai.plugin.socket.pack.PackBase;
import net.mamoe.mirai.message.data.Message;

import java.util.ArrayList;
import java.util.List;

/*
48 [机器人]在发送群临时会话消息前广播（事件）
id:群号
fid:发送人的QQ号
message:消息
 */
public class TempMessagePreSendEventPack extends PackBase {
    public long id;
    public long fid;
    public List<String> message;

    public TempMessagePreSendEventPack(long qq, long id, long fid, Message message) {
        this.fid = fid;
        this.qq = qq;
        this.id = id;
        this.message = new ArrayList<>();
        this.message.add(message.toString());
        this.message.add(message.contentToString());
    }
}
