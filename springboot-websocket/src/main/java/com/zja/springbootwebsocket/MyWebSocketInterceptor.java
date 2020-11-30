package com.zja.springbootwebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-21 2:08
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：自定义拦截器
 */
@Slf4j
public class MyWebSocketInterceptor implements HandshakeInterceptor {

    /**
     * 握手前拦截  一般用来注册用户信息，绑定 WebSocketSession
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("握手前拦截");

        if (!(request instanceof ServletServerHttpRequest)) {
            return true;
        }

//            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
//            String userName = (String) servletRequest.getSession().getAttribute("userName");
        String userName = "Koishipyb";
        attributes.put("userName", userName);

        return true;
    }


    /**
     * 握手后拦截
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        log.info("握手后拦截");
    }
}
