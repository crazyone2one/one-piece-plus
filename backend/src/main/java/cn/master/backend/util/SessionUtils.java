package cn.master.backend.util;

import cn.master.backend.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author create by 11's papa on 2023/1/5-13:07
 */
@Slf4j
public class SessionUtils {
    public static String getCurrentWorkspaceId() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            log.debug("WORKSPACE: {}", request.getHeader("WORKSPACE"));
            if (Objects.nonNull(request.getHeader("WORKSPACE"))) {
                return request.getHeader("WORKSPACE");
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static String getCurrentProjectId() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            log.debug("PROJECT: {}", request.getHeader("PROJECT"));
            if (Objects.nonNull(request.getHeader("PROJECT"))) {
                return request.getHeader("PROJECT");
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public static String getUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        JwtUtils jwtUtils = SpringContextUtils.getBean(JwtUtils.class);
        assert jwtUtils != null;
        return jwtUtils.getUserIdFromToken(request);
    }
}
