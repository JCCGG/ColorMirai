package coloryr.colormirai.demo.sdk.pack.re;

import coloryr.colormirai.demo.sdk.enums.MemberPermission;

/*
id:群号
name:群名
img:群头像
oid:所有者QQ号
per:机器人所拥有的权限
 */
public class GroupInfo {
    public long id;
    public String name;
    public String img;
    public long oid;
    public MemberPermission per;
}
