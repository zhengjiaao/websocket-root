package com.zja.app;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class AppWebSocketUtilnew {
    //静态方法确保每个类生成的对象都只有一个map，ConcurrentHashMap确保并发安全
    //连接池-----WebSocket 连接 ， String 设备code
    private static ConcurrentHashMap<WebSocketHandler, String> deviceConnections = new ConcurrentHashMap<>();

    //发送消息给所有的连接者
    public static void sendToAll(String text) {
        deviceConnections.keySet()
                .stream()
                .forEach(client -> {
                    try {
                        client.send(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 添加设备连接
     *
     * @param deviceCode
     * @param conn
     */
    public static void addDeviceConnections(String deviceCode, WebSocketHandler conn) {
        deviceConnections.put(conn, deviceCode);
    }

    /**
     * 删除设备连接
     *
     * @param conn
     * @return
     */
    public static boolean removeDeviceConnections(WebSocketHandler conn) {
        if (deviceConnections.containsKey(conn)) {
            deviceConnections.remove(conn); // 移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取所有设备连接
     *
     * @return
     */
    public static Set<WebSocketHandler> getAllWebSocket() {
        return deviceConnections.keySet();
    }

    /**
     * 根据连接获取在线设备
     *
     * @param conn
     * @return
     */
    public static String getDeviceByWebSocket(WebSocketHandler conn) {
        return deviceConnections.get(conn);
    }

    /**
     * 获取所有在线设备
     *
     * @return
     */
    public static Collection<String> getAllDevices() {
        List<String> deviceCollection = new ArrayList<String>();
        Collection<String> deviceCodes = deviceConnections.values();
        for (String deviceCode : deviceCodes) {
            deviceCollection.add(deviceCode);
        }
        return deviceCollection;
    }

    /**
     * 获取在线设备数量
     *
     * @return
     */
    public static int getDevicesCount() {
        return deviceConnections.size();
    }

    /**
     * 根据设备获取连接
     *
     * @param deviceCode
     * @return
     */
    public static WebSocketHandler getWebSocketByDevice(String deviceCode) {
        Set<WebSocketHandler> keySet = deviceConnections.keySet();
        synchronized (keySet) {
            for (WebSocketHandler conn : keySet) {
                String cuser = deviceConnections.get(conn);
                if (cuser.equals(deviceCode)) {
                    return conn;
                }
            }
        }
        return null;
    }

    /**
     * 向指定在线设备发送消息
     *
     * @param conn
     * @param message
     * @return
     */
    public static void sendMessageToOnlineDevice(WebSocketHandler conn, String message) throws IOException {
        if (null != conn && null != deviceConnections.get(conn)) {
            conn.send(message);
        }
    }

    /**
     * 向所有在线设备发送信息
     *
     * @param message
     */
    public static void sendMessageToOnlineAllDevice(String message) throws IOException {
        Set<WebSocketHandler> keySet = deviceConnections.keySet();
        synchronized (keySet) {
            for (WebSocketHandler conn : keySet) {
                String deviceCode = deviceConnections.get(conn);
                if (deviceCode != null) {
                    conn.send(message);
                }
            }
        }
    }
}
