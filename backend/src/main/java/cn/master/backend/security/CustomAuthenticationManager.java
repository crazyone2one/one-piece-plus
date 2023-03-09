package cn.master.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import java.util.Collections;

/**
 * @author create by 11's papa on 2022/12/29-14:23
 */
@Configuration
@RequiredArgsConstructor
public class CustomAuthenticationManager {
    final AuthenticationProvider authenticationProvider;
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }
}
