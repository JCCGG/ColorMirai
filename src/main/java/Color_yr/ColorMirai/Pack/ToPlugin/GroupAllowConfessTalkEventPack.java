package Color_yr.ColorMirai.Pack.ToPlugin;

import Color_yr.ColorMirai.Pack.PackBase;

/*
25 [机器人]群 "坦白说" 功能状态改变（事件）
id：群号
fid：执行人QQ号
old：旧的状态
new_：新的状态
 */
public class GroupAllowConfessTalkEventPack extends PackBase {
    public long id;
    public boolean old;
    public boolean new_;
    public boolean bot;

    public GroupAllowConfessTalkEventPack(long qq, long id, boolean old, boolean new_, boolean bot) {
        this.id = id;
        this.qq = qq;
        this.old = old;
        this.new_ = new_;
        this.bot = bot;
    }
}
