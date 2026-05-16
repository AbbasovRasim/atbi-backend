package com.rasim.dax.config;

import com.rasim.dax.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity  // ✅ BUNU ƏLAVƏ ET
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // LOGIN hərkəsə açıq
                        .requestMatchers("/auth/login").permitAll()

                        // ✅ REGISTER - HƏR KƏSƏ AÇIQ (MÜVƏQQƏTİ)
                        .requestMatchers("/auth/register").permitAll()

                        // OPTIONS requestlər
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // QALAN HƏR ŞEY - LOGIN TƏLƏB EDİR
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}