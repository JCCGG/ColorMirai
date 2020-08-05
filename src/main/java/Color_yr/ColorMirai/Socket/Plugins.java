package Color_yr.ColorMirai.Socket;

import Color_yr.ColorMirai.BotStart;
import Color_yr.ColorMirai.EventDo.EventCall;
import Color_yr.ColorMirai.Pack.FromPlugin.*;
import Color_yr.ColorMirai.Pack.PackStart;
import Color_yr.ColorMirai.Start;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Plugins {
    private final Socket Socket;
    private final List<byte[]> Tasks = new CopyOnWriteArrayList<>();
    private final Gson Gson;
    private String name;
    private Thread read;
    private Thread doRead;
    private List<Byte> Events = null;
    private boolean isRun;

    public Plugins(Socket Socket) {
        this.Socket = Socket;
        Gson = new Gson();
        try {
            byte[] buf = new byte[8192];
            int len = Socket.getInputStream().read(buf);
            if (len > 0) {
                String temp = new String(buf, StandardCharsets.UTF_8);
                PackStart pack = new Gson().fromJson(temp, PackStart.class);
                if (name != null && Events != null) {
                    name = pack.getName();
                    Events = pack.getReg();
                    SocketServer.addPlugin(name, this);
                } else {
                    Start.logger.warn("插件连接初始化失败");
                    return;
                }
            } else {
                Start.logger.warn("插件连接初始化失败");
                return;
            }
        } catch (IOException e) {
            Start.logger.error("插件连接初始化失败", e);
            return;
        }
        isRun = true;
        read = new Thread(() -> {
            while (isRun) {
                try {
                    if (Socket.getInputStream().available() > 0) {
                        byte[] buf = new byte[Socket.getInputStream().available()];
                        int len1 = Socket.getInputStream().read(buf);
                        if (len1 > 0) {
                            Tasks.add(buf);
                        } else if (len1 == -1) {
                            Start.logger.error("连接发生异常");
                            close();
                            break;
                        }
                    }
                    Thread.sleep(50);
                } catch (Exception e) {
                    if (!isRun)
                        break;
                    Start.logger.error("连接发生异常", e);
                    close();
                }
            }
        });
        read.start();
        doRead = new Thread(() -> {
            while (isRun) {
                try {
                    if (!Tasks.isEmpty()) {
                        byte[] buf = Tasks.remove(0);
                        byte type = buf[buf.length - 1];
                        buf[buf.length - 1] = 0;
                        String a = new String(buf, StandardCharsets.UTF_8);
                        if (!a.isEmpty()) {
                            switch (type) {
                                case 52:
                                    SendGroupMessagePack pack = Gson.fromJson(a, SendGroupMessagePack.class);
                                    BotStart.sendGroupMessage(pack.getId(), pack.getMessage());
                                    break;
                                case 53:
                                    SendGroupPrivateMessagePack pack1 = Gson.fromJson(a, SendGroupPrivateMessagePack.class);
                                    BotStart.sendGroupPrivateMessage(pack1.getId(), pack1.getFid(), pack1.getMessage());
                                    break;
                                case 54:
                                    SendFriendMessage pack2 = Gson.fromJson(a, SendFriendMessage.class);
                                    BotStart.sendFriendMessage(pack2.getId(), pack2.getMessage());
                                    break;
                                case 55:
                                    String groups = Gson.toJson(BotStart.getGroups());
                                    byte[] temp = groups.getBytes(StandardCharsets.UTF_8);
                                    byte[] temp1 = new byte[temp.length + 1];
                                    temp1[temp.length] = 52;
                                    SocketServer.sendPack(temp1, Socket);
                                    break;
                                case 56:
                                    String friends = Gson.toJson(BotStart.getFriends());
                                    byte[] temp2 = friends.getBytes(StandardCharsets.UTF_8);
                                    byte[] temp3 = new byte[temp2.length + 1];
                                    temp3[temp2.length] = 53;
                                    SocketServer.sendPack(temp3, Socket);
                                    break;
                                case 57:
                                    GetGroupMemberInfo pack3 = Gson.fromJson(a, GetGroupMemberInfo.class);
                                    String members = Gson.toJson(BotStart.getMembers(pack3.getId()));
                                    byte[] temp4 = members.getBytes(StandardCharsets.UTF_8);
                                    byte[] temp5 = new byte[temp4.length + 1];
                                    temp5[temp4.length] = 54;
                                    SocketServer.sendPack(temp5, Socket);
                                    break;
                                case 58:
                                    GetGroupSettingPack pack4 = Gson.fromJson(a, GetGroupSettingPack.class);
                                    String groupinfo = Gson.toJson(BotStart.getGroupInfo(pack4.getId()));
                                    byte[] temp6 = groupinfo.getBytes(StandardCharsets.UTF_8);
                                    byte[] temp7 = new byte[temp6.length + 1];
                                    temp7[temp6.length] = 55;
                                    SocketServer.sendPack(temp7, Socket);
                                    break;
                                case 59:
                                    EventCallPack pack5 = Gson.fromJson(a, EventCallPack.class);
                                    EventCall.DoEvent(pack5.getEventid(), pack5.getDofun(), pack5.getArg());
                                    break;
                            }
                        }
                    }
                    Thread.sleep(50);
                } catch (Exception e) {
                    if (!isRun)
                        break;
                    Start.logger.error("数据处理发生异常", e);
                    close();
                }
            }
        });
        doRead.start();
    }

    public void callEvent(byte index, byte[] data) {
        if (Events.contains(index)) {
            SocketServer.sendPack(data, Socket);
        }
    }

    public void close() {
        try {
            isRun = false;
            Socket.close();
            if (read != null && read.isAlive())
                read.join();
            if (doRead != null && doRead.isAlive())
                doRead.join();
            SocketServer.removePlugin(name);
        } catch (Exception e) {
            Start.logger.error("插件断开失败", e);
        }
    }
}
