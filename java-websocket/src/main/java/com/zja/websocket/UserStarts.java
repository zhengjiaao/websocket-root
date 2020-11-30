package com.zja.websocket;

import com.zja.websocket.model.WebSocketPorts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-20 20:16
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Slf4j
@Component
public class UserStarts implements InitializingBean {

    @Resource
    private WebSocketPorts ports;

    @Bean
    public UserChatWebSocketServer webSocketChatServer() throws UnknownHostException {
        return new UserChatWebSocketServer(ports.getUserProt());
    }

    @Autowired
    UserChatWebSocketServer webSocketChatServer;

    @Override
    public void afterPropertiesSet() {
        try {
            webSocketChatServer.start();
            log.info("聊天房间创建完成，房间号为:  {}", webSocketChatServer.getPort());
        } catch (Exception e) {
            log.error("聊天房间创建失败，房间号为:  {}", ports.getAppProt());
        }
    }
}
