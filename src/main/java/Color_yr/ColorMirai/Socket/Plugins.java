package Color_yr.ColorMirai.Socket;

import Color_yr.ColorMirai.EventDo.EventCall;
import Color_yr.ColorMirai.Pack.FromPlugin.*;
import Color_yr.ColorMirai.Pack.PackDo;
import Color_yr.ColorMirai.Pack.ReturnPlugin.FriendsPack;
import Color_yr.ColorMirai.Pack.ReturnPlugin.GroupsPack;
import Color_yr.ColorMirai.Pack.ReturnPlugin.MemberInfoPack;
import Color_yr.ColorMirai.Robot.BotStart;
import Color_yr.ColorMirai.Start;
import com.alibaba.fastjson.JSON;
import net.mamoe.mirai.contact.GroupSettings;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Plugins {
    private final Socket Socket;
    private final List<RePackTask> Tasks = new CopyOnWriteArrayList<>();
    private final List<Long> Groups = new CopyOnWriteArrayList<>();
    private final List<Long> QQs = new CopyOnWriteArrayList<>();
    private final Thread read;
    private final Thread doRead;
    private String name;
    private long runQQ;
    private List<Integer> Events = null;
    private boolean isRun;

    public Plugins(Socket Socket) {
        this.Socket = Socket;
        try {
            this.Socket.setTcpNoDelay(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        read = new Thread(this::start);
        doRead = new Thread(this::startRead);
        read.start();
    }

    public String getName() {
        return name;
    }

    public String getReg() {
        if (Events.size() == 0)
            return "无";
        StringBuilder stringBuilder = new StringBuilder();
        for (int item : Events) {
            stringBuilder.append(item).append(",");
        }
        String data = stringBuilder.toString();
        return data.substring(0, data.length() - 1);
    }

    private void startRead() {
        while (isRun) {
            try {
                if (!Tasks.isEmpty()) {
                    RePackTask task = Tasks.remove(0);
                    switch (task.index) {
                        case 52:
                            SendGroupMessagePack pack = JSON.parseObject(task.data, SendGroupMessagePack.class);
                            BotStart.sendGroupMessage(runQQ == 0 ? pack.qq : runQQ, pack.id, pack.message);
                            break;
                        case 53:
                            SendGroupPrivateMessagePack pack1 = JSON.parseObject(task.data, SendGroupPrivateMessagePack.class);
                            BotStart.sendGroupPrivateMessage(runQQ == 0 ? pack1.qq : runQQ, pack1.id, pack1.fid, pack1.message);
                            break;
                        case 54:
                            SendFriendMessagePack pack2 = JSON.parseObject(task.data, SendFriendMessagePack.class);
                            BotStart.sendFriendMessage(runQQ == 0 ? pack2.qq : runQQ, pack2.id, pack2.message);
                            break;
                        case 55:
                            GetPack pack17 = JSON.parseObject(task.data, GetPack.class);
                            List<GroupsPack> data = BotStart.getGroups(runQQ == 0 ? pack17.qq : runQQ);
                            if (data == null)
                                break;
                            if (SocketServer.sendPack(PackDo.BuildPack(data, 55), Socket))
                                close();
                            break;
                        case 56:
                            GetPack pack18 = JSON.parseObject(task.data, GetPack.class);
                            List<FriendsPack> data1 = BotStart.getFriends(runQQ == 0 ? pack18.qq : runQQ);
                            if (data1 == null)
                                break;
                            if (SocketServer.sendPack(PackDo.BuildPack(data1, 56), Socket))
                                close();
                            break;
                        case 57:
                            GetGroupMemberInfoPack pack3 = JSON.parseObject(task.data, GetGroupMemberInfoPack.class);
                            List<MemberInfoPack> data2 = BotStart.getMembers(runQQ == 0 ? pack3.qq : runQQ, pack3.id);
                            if (data2 == null)
                                break;
                            if (SocketServer.sendPack(PackDo.BuildPack(data2, 57), Socket))
                                close();
                            break;
                        case 58:
                            GetGroupSettingPack pack4 = JSON.parseObject(task.data, GetGroupSettingPack.class);
                            GroupSettings data3 = BotStart.getGroupInfo(runQQ == 0 ? pack4.qq : runQQ, pack4.id);
                            if (data3 == null)
                                break;
                            if (SocketServer.sendPack(PackDo.BuildPack(data3, 58), Socket))
                                close();
                            break;
                        case 59:
                            EventCallPack pack5 = JSON.parseObject(task.data, EventCallPack.class);
                            EventCall.DoEvent(runQQ == 0 ? pack5.qq : runQQ, pack5.eventid, pack5.dofun, pack5.arg);
                            break;
                        case 61:
                            Map<String, String> formdata = DataFrom.parse(task.data);
                            if (formdata.containsKey("id") && formdata.containsKey("img") && formdata.containsKey("qq")) {
                                try {
                                    long id = Long.parseLong(formdata.get("id"));
                                    long qq = Long.parseLong(formdata.get("qq"));
                                    BotStart.sendGroupImage(runQQ == 0 ? qq : runQQ, id, formdata.get("img"));
                                } catch (Exception e) {
                                    Start.logger.error("解析发生错误", e);
                                }
                            }
                            break;
                        case 62:
                            Map<String, String> formdata1 = DataFrom.parse(task.data);
                            if (formdata1.containsKey("id") && formdata1.containsKey("fid") && formdata1.containsKey("img") && formdata1.containsKey("qq")) {
                                try {
                                    long id = Long.parseLong(formdata1.get("id"));
                                    long fid = Long.parseLong(formdata1.get("fid"));
                                    long qq = Long.parseLong(formdata1.get("qq"));
                                    BotStart.sendGroupPrivataImage(runQQ == 0 ? qq : runQQ, id, fid, formdata1.get("img"));
                                } catch (Exception e) {
                                    Start.logger.error("解析发生错误", e);
                                }
                            }
                            break;
                        case 63:
                            Map<String, String> formdata2 = DataFrom.parse(task.data);
                            if (formdata2.containsKey("id") && formdata2.containsKey("img")) {
                                try {
                                    long id = Long.parseLong(formdata2.get("id"));
                                    long qq = Long.parseLong(formdata2.get("qq"));
                                    BotStart.sendFriendImage(runQQ == 0 ? qq : runQQ, id, formdata2.get("img"));
                                } catch (Exception e) {
                                    Start.logger.error("解析发生错误", e);
                                }
                            }
                            break;
                        case 64:
                            DeleteGroupMemberPack pack9 = JSON.parseObject(task.data, DeleteGroupMemberPack.class);
                            BotStart.DeleteGroupMember(runQQ == 0 ? pack9.qq : runQQ, pack9.id, pack9.fid);
                            break;
                        case 65:
                            MuteGroupMemberPack pack10 = JSON.parseObject(task.data, MuteGroupMemberPack.class);
                            BotStart.MuteGroupMember(runQQ == 0 ? pack10.qq : runQQ, pack10.id, pack10.fid, pack10.time);
                            break;
                        case 66:
                            UnmuteGroupMemberPack pack11 = JSON.parseObject(task.data, UnmuteGroupMemberPack.class);
                            BotStart.UnmuteGroupMember(runQQ == 0 ? pack11.qq : runQQ, pack11.id, pack11.fid);
                            break;
                        case 67:
                            GroupMuteAllPack pack12 = JSON.parseObject(task.data, GroupMuteAllPack.class);
                            BotStart.GroupMuteAll(runQQ == 0 ? pack12.qq : runQQ, pack12.id);
                            break;
                        case 68:
                            GroupUnmuteAllPack pack13 = JSON.parseObject(task.data, GroupUnmuteAllPack.class);
                            BotStart.GroupUnmuteAll(runQQ == 0 ? pack13.qq : runQQ, pack13.id);
                            break;
                        case 69:
                            SetGroupMemberCard pack14 = JSON.parseObject(task.data, SetGroupMemberCard.class);
                            BotStart.SetGroupMemberCard(runQQ == 0 ? pack14.qq : runQQ, pack14.id, pack14.fid, pack14.card);
                            break;
                        case 70:
                            SetGroupNamePack pack15 = JSON.parseObject(task.data, SetGroupNamePack.class);
                            BotStart.SetGroupName(runQQ == 0 ? pack15.qq : runQQ, pack15.id, pack15.name);
                            break;
                        case 71:
                            ReCallMessagePack pack16 = JSON.parseObject(task.data, ReCallMessagePack.class);
                            BotStart.ReCall(pack16.id);
                            break;
                        case 74:
                            Map<String, String> formdata3 = DataFrom.parse(task.data);
                            if (formdata3.containsKey("id") && formdata3.containsKey("sound") && formdata3.containsKey("qq")) {
                                try {
                                    long id = Long.parseLong(formdata3.get("id"));
                                    long qq = Long.parseLong(formdata3.get("qq"));
                                    BotStart.SendGroupSound(runQQ == 0 ? qq : runQQ, id, formdata3.get("sound"));
                                } catch (Exception e) {
                                    Start.logger.error("解析发生错误", e);
                                }
                            }
                            break;
                        case 75:
                            LoadFileSendToGroupImagePack pack19 = JSON.parseObject(task.data, LoadFileSendToGroupImagePack.class);
                            BotStart.sendGroupImageFile(runQQ == 0 ? pack19.qq : runQQ, pack19.id, pack19.file);
                            break;
                        case 76:
                            LoadFileSendToGroupPrivateImagePack pack20 = JSON.parseObject(task.data, LoadFileSendToGroupPrivateImagePack.class);
                            BotStart.sendGroupPrivateImageFile(runQQ == 0 ? pack20.qq : runQQ, pack20.id, pack20.fid, pack20.file);
                            break;
                        case 77:
                            LoadFileSendToFriendImagePack pack21 = JSON.parseObject(task.data, LoadFileSendToFriendImagePack.class);
                            BotStart.sendFriendImageFile(runQQ == 0 ? pack21.qq : runQQ, pack21.id, pack21.file);
                            break;
                        case 78:
                            LoadFileSendToGroupSoundPack pack22 = JSON.parseObject(task.data, LoadFileSendToGroupSoundPack.class);
                            BotStart.SendGroupSoundFile(runQQ == 0 ? pack22.qq : runQQ, pack22.id, pack22.file);
                            break;
                        case 83:
                            FriendNudgePack pack23 = JSON.parseObject(task.data, FriendNudgePack.class);
                            BotStart.SendNudge(runQQ == 0 ? pack23.qq : runQQ, pack23.id);
                            break;
                        case 84:
                            MemberNudgePack pack24 = JSON.parseObject(task.data, MemberNudgePack.class);
                            BotStart.SendNudge(runQQ == 0 ? pack24.qq : runQQ, pack24.id, pack24.fid);
                            break;
                        case 85:
                            GetGroupHonorListDataPack pack25 = JSON.parseObject(task.data, GetGroupHonorListDataPack.class);
                            BotStart.GetGroupHonorListData(runQQ == 0 ? pack25.qq : runQQ, pack25.id);
                            break;
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                if (!isRun)
                    break;
                Start.logger.error("数据处理发生异常", e);
                close();
            }
        }
    }

    private void start() {
        try {
            while (Socket.getInputStream().available() == 0) {
                Thread.sleep(10);
            }
            byte[] buf = new byte[Socket.getInputStream().available()];
            int len = Socket.getInputStream().read(buf);
            if (len > 0) {
                String temp = new String(buf, StandardCharsets.UTF_8);
                StartPack pack = JSON.parseObject(temp, StartPack.class);
                if (pack.Name != null && pack.Reg != null) {
                    name = pack.Name;
                    Events = pack.Reg;
                    if (pack.Groups != null) {
                        Groups.addAll(pack.Groups);
                    }
                    if (pack.QQs != null) {
                        QQs.addAll(pack.QQs);
                    }
                    if (pack.RunQQ != 0 && !BotStart.getBots().contains(pack.RunQQ)) {
                        Start.logger.warn("插件连接失败，没有运行的QQ：" + pack.RunQQ);
                        Socket.close();
                        return;
                    }
                    runQQ = pack.RunQQ;
                    SocketServer.addPlugin(name, this);
                    String data = JSON.toJSONString(BotStart.getBots());
                    SocketServer.sendPack(data.getBytes(StandardCharsets.UTF_8), this.Socket);
                } else {
                    Start.logger.warn("插件连接初始化失败");
                    Socket.close();
                    return;
                }
            } else {
                Start.logger.warn("插件连接初始化失败");
                return;
            }
        } catch (Exception e) {
            Start.logger.error("插件连接初始化失败", e);
            return;
        }
        isRun = true;
        doRead.start();
        while (isRun) {
            try {
                if (Socket.getInputStream().available() > 0) {
                    InputStream inputStream = Socket.getInputStream();
                    byte[] bytes = new byte[8192];
                    int len;
                    byte index = 0;
                    StringBuilder sb = new StringBuilder();
                    while ((len = inputStream.read(bytes)) != -1) {
                        if (len == 8192)
                            sb.append(new String(bytes, 0, len, StandardCharsets.UTF_8));
                        else {
                            index = bytes[len - 1];
                            bytes[len - 1] = 0;
                            sb.append(new String(bytes, 0, len - 1, StandardCharsets.UTF_8));
                            break;
                        }
                    }
                    Tasks.add(new RePackTask(index, sb.toString()));
                } else if (Socket.getInputStream().available() < 0) {
                    Start.logger.warn("插件连接断开");
                    close();
                    return;
                }
                Thread.sleep(10);
            } catch (Exception e) {
                if (!isRun)
                    break;
                Start.logger.error("连接发生异常", e);
                close();
            }
        }
    }

    public void callEvent(SendPackTask task, byte[] data) {
        if (runQQ != 0 && task.runqq != runQQ)
            return;
        if (Groups.size() != 0 && task.group != 0 && !Groups.contains(task.group))
            return;
        if (QQs.size() != 0 && task.qq != 0 && !QQs.contains(task.qq))
            return;
        if (Events.contains((int) task.index) || task.index == 60) {
            if (SocketServer.sendPack(data, Socket))
                close();
        }
    }

    public void close() {
        try {
            isRun = false;
            Socket.close();
            SocketServer.removePlugin(name);
        } catch (Exception e) {
            Start.logger.error("插件断开失败", e);
        }
    }
}
