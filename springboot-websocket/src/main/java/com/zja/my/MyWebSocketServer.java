package com.zja.my;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-20 17:32
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Java WebSocket 服务端
 */
@Slf4j
@ServerEndpoint(value = "/ws/serverTree")
@Component
public class MyWebSocketServer {

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立后触发的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("onOpen" + session.getId());
        MyWebSocketMapUtil.put(session.getId(), this);
    }


    /**
     * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        MyWebSocketMapUtil.remove(session.getId());
        log.info("====== onClose:" + session.getId() + " ======");
    }


    /**
     * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String params, Session session) throws Exception {
        //获取服务端到客户端的通道
        MyWebSocketServer myWebSocket = MyWebSocketMapUtil.get(session.getId());
        log.info("收到来自" + session.getId() + "的消息" + params);
        String result = "收到来自" + session.getId() + "的消息" + params;
        //返回消息给Web Socket客户端（浏览器）
        myWebSocket.sendMessage(1, "成功！", result);
    }


    /**
     * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();
    }

    public void sendMessage(int status, String message, Object datas) throws IOException {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        result.put("datas", datas);
        this.session.getBasicRemote().sendText(result.toString());
    }
}
