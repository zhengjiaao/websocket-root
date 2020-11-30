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
public class AppStarts implements InitializingBean {

    @Resource
    private WebSocketPorts ports;

    @Bean
    public AppVersionWebSocketServer webSocketAppVersionServer() throws UnknownHostException {
        return new AppVersionWebSocketServer(ports.getAppProt());
    }

    @Autowired
    AppVersionWebSocketServer webSocketAppVersionServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            webSocketAppVersionServer.start();
            log.info("App版本推送房间已开启,等待客户端接入的端口号:  {}", webSocketAppVersionServer.getPort());
        } catch (Exception e) {
            log.info("App版本推房间创建异常，端口为:  {}", ports.getAppProt());
        }
    }
}
