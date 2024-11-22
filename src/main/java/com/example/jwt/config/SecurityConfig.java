package com.example.jwt.config;

import com.example.jwt.filter.*;
import com.example.jwt.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private UserDetailService uds;
    private JwtAuthenticationFilter af;
    private final LoggingFilter lf;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(lf, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(r -> r.requestMatchers("/login", "/register").permitAll()
                        .anyRequest().authenticated()).formLogin(form -> form.loginPage("/login").permitAll()
                        .successHandler((request, response, authentication) -> {
                            logger.info("Successful login for user: {}", authentication.getName());
                        }).failureHandler((request, response, exception) -> {
                            logger.warn("Failed login attempt: {}", request.getParameter("username"));
                        })).logout(logout -> logout.logoutUrl("/logout").permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(uds);
        ap.setPasswordEncoder(passwordEncoder());
        return ap;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}
