package com.fd.config;

import com.fd.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtAuthenticationTokenFilter authenticationTokenFilter) {
        this.authenticationTokenFilter = authenticationTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationTokenFilter authenticationTokenFilter;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // 配置 Session 为无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 登录接口允许匿名访问
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/book/**").anonymous()
                        .requestMatchers("/word/{id}").anonymous()
                        .requestMatchers("/word/listWords").authenticated()
                        // 其余所有请求都需要认证
                        .anyRequest().authenticated()
                )
                .logout(AbstractHttpConfigurer::disable)
                //允许跨域
                .cors(Customizer.withDefaults());

        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(e ->
                e.authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));
        return http.build();

    }


}