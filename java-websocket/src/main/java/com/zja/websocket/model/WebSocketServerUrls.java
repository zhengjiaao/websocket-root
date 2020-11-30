package com.zja.websocket.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-11-20 20:44
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
@Component
@ConfigurationProperties(prefix = "websocket.url")
public class WebSocketServerUrls implements Serializable {
    private String myUrl;
    private String appUrl;
}
