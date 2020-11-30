package com.zja.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * WebSocket推送测试  端口： 8887
 * @author zhengja@dist.com.cn
 * @data 2019/4/9 10:42
 */
@Slf4j
public class UserChatWebSocketServer extends WebSocketServer {

    private String username;

    public UserChatWebSocketServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public UserChatWebSocketServer(InetSocketAddress address) {
        super(address);
        log.info("InetSocketAddress: {}", address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String hostAddress = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        log.info("有人进入房间，conn：{}  hostAddress：{}", conn, hostAddress);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String hostAddress = conn.getRemoteSocketAddress().getAddress().getHostAddress();
        log.info("有人离开房间，conn：{}  hostAddress：{}", conn, hostAddress);
        //触发关闭事件
        userLeave(conn);
    }

    //消息发送
    @Override
    public void onMessage(WebSocket webSocket, String s) {
        if (null != s && s.startsWith("online")) {
            String[] msg = s.split(",", 2);
            UserWebSocketUtil.addUser(msg[1], webSocket); //接入到连接池

            String online = "[系统提示]：" + msg[1] + " 上线了";
            log.info(online);
            sendToAll(online);
        } else if (null != s && s.startsWith("offline")) {
            UserWebSocketUtil.removeUser(webSocket); //从连接池中移除

            sendToAll(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " 断开连接 ！");
            //断开连接触发
            boolean deviceConnections = UserWebSocketUtil.removeUser(webSocket);
            if (deviceConnections) {
                System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " 断开连接成功 ！");
            } else {
                System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " 连接已经失效 ！");
            }
        } else {
            String[] msg = s.split("@", 2);//以@为分隔符把字符串分为xxx和xxxxx两部分,msg[0]表示发送至的用户名，all则表示发给所有人
            if (msg[0].equals("all")) {
                UserWebSocketUtil.sendMessageToOnlineAllUser(msg[1]);
            } else {
                //指定用户发送消息
                sendMessageToUser(webSocket, msg[0], msg[1]);
            }
        }
    }

    //异常抛出
    @Override
    public void onError(WebSocket conn, Exception e) {
        e.printStackTrace();
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * 用户下线处理
     * @param conn
     */
    public void userLeave(WebSocket conn) {
        String user = UserWebSocketUtil.getUserByKey(conn);
        // 在连接池中移除连接
        boolean b = UserWebSocketUtil.removeUser(conn);
        if (b) {
            // 把当前用户从所有在线用户列表中删除
            UserWebSocketUtil.sendMessageToOnlineAllUser(user);
            String leaveMsg = "[系统提示]：" + user + " 离开了";
            log.info(leaveMsg);
            // 向在线用户发送当前用户退出的信息
            UserWebSocketUtil.sendMessageToOnlineAllUser(leaveMsg);
        }
    }

    /**
     * 用户上线处理
     * @param user
     * @param conn
     */
    public void userJoin(String user, WebSocket conn) {
        // 把当前用户加入到所有在线用户列表中
        UserWebSocketUtil.sendMessageToOnlineAllUser(user);
        String joinMsg = "[系统提示]：" + user + " 上线了";
        log.info(joinMsg);
        // 向所有在线用户推送当前用户上线的消息
        UserWebSocketUtil.sendMessageToOnlineAllUser(joinMsg);
        // 向连接池添加当前的连接的对象
        UserWebSocketUtil.addUser(user, conn);
        // 向当前连接发送当前在线用户的列表
        UserWebSocketUtil.sendMessageToOnlineUser(conn, UserWebSocketUtil.getOnlineUser().toString());
    }

    /**
     * 发送消息给指定用户
     * @param user
     * @param message
     */
    public void sendMessageToUser(WebSocket currentConnection, String user, String message) {
        //获取在线用户的连接
        WebSocket conn = UserWebSocketUtil.getWebSocketByUser(user);
        if (null != conn) {
            //向在线特定用户发送消息
            UserWebSocketUtil.sendMessageToOnlineUser(conn, message);
            //同时发送消息给当前用户
            UserWebSocketUtil.sendMessageToOnlineUser(currentConnection, message);
        } else {
            String msg = "[系统提示]：" + user + " 用户不在线,请您稍后发送！";
            log.info(msg);
            UserWebSocketUtil.sendMessageToOnlineUser(currentConnection, msg);
        }
    }

    // 发送给所有的在线用户
    private void sendToAll(String text) {
        Collection<WebSocket> conns = connections();
        synchronized (conns) {
            for (WebSocket client : conns) {
                client.send(text);
            }
        }
    }

}
