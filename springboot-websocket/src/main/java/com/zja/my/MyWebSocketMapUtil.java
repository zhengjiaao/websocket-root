package com.zja.my;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-20 17:34
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class MyWebSocketMapUtil {

    public static ConcurrentMap<String, MyWebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    public static void put(String key, MyWebSocketServer myWebSocketServer) {
        webSocketMap.put(key, myWebSocketServer);
    }

    public static MyWebSocketServer get(String key) {
        return webSocketMap.get(key);
    }

    public static void remove(String key) {
        webSocketMap.remove(key);
    }

    public static Collection<MyWebSocketServer> getValues() {
        return webSocketMap.values();
    }
}
