package cn.master.backend.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt 相关参数
 *
 * @author create by 11's papa on 2022/12/27-14:17
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String authorization;
    private String secretKey;
    private String tokenPrefix;
    private long expirationTime;
}
