package com.nc13.spring_board.config;

import com.nc13.spring_board.service.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, UserAuthService userAuthService) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // Cross Site Request Forgery
                .authorizeHttpRequests((authorize) -> authorize // URL 별 권한 설정
                        .requestMatchers("/WEB-INF/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/user/*", "/").permitAll() // 누구나 접근 가능
                        .requestMatchers("/board/write").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                // 로그인에서 사용할 페이지 설정
                .formLogin((formLogin) -> formLogin
                        .loginPage("/")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/board/showAll/1")
                        .loginProcessingUrl("/user/auth"))
                .userDetailsService(userAuthService);


        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
