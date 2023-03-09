package cn.master.backend.security;

import cn.master.backend.config.ResponseInfo;
import cn.master.backend.filter.JwtRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author create by 11's papa on 2022/12/27-14:24
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/user/login", "/user/register").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();
        // Disable CSRF (cross site request forgery)
        httpSecurity.csrf().disable();
        //添加退出操作和退出成功操作
        // https://docs.spring.io/spring-security/reference/5.7.6/servlet/authentication/logout.html
        httpSecurity.logout(logout -> logout.logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=utf-8");
                            response.getWriter().write(new ObjectMapper().writeValueAsString(
                                    new ResponseInfo<>(200, "退出成功！")
                            ));
                        })
                        .logoutUrl("/user/logout")
                        .invalidateHttpSession(true)
                        .addLogoutHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        );
        // 禁用Session sessionCreationPolicy session创建策略，【前后端分离 session就没用了】
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.headers()
                .frameOptions()
                .sameOrigin();
        //过滤器前置配置
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
