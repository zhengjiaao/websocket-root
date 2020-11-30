package com.zja.springbootwebsocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-21 0:03
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Configuration
@EnableWebSocket  //用于开启注解接收和发送消息
public class WebSocketConfigTwo implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //注册自定义处理器和拦截器，并设置路径
        registry.addHandler(new MyWebSocketHandler(), "/ws/serverTwo")
                .setAllowedOrigins("*")
                .addInterceptors(new MyWebSocketInterceptor());
    }

}
