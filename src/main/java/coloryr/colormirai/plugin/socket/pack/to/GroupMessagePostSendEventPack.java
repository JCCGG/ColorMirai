package coloryr.colormirai.plugin.socket.pack.to;

import coloryr.colormirai.plugin.socket.pack.PackBase;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.SingleMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 28 [机器人]在群消息发送后广播（事件）
 */
public class GroupMessagePostSendEventPack extends PackBase {
    /**
     * 群号
     */
    public long id;
    /**
     * 是否发送成功
     */
    public boolean res;
    /**
     * 发送的消息
     */
    public List<String> message;
    /**
     * 错误消息
     */
    public String error;

    public GroupMessagePostSendEventPack(long qq, long id, boolean res, MessageSource message, String error) {
        this.error = error;
        this.id = id;
        this.qq = qq;
        this.message = new ArrayList<>();
        if (message != null) {
            this.message.add(message.toString());
            for (SingleMessage item : message.getOriginalMessage()) {
                this.message.add(item.toString());
            }
        }
        this.res = res;
    }
}
