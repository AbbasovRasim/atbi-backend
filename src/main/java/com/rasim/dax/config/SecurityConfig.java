package com.rasim.dax.config;

import com.rasim.dax.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
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

                        // REGISTER yalnız ADMIN üçün
                        // MÜVƏQQƏTİ - YALNIZ TEST ÜÇÜN
                        .requestMatchers("/auth/register").permitAll()  // hasAuthority("ADMIN") əvəzinə

                        // OPTIONS requestlər
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Incident endpointləri yalnız login olmuş userlər üçün
                        .requestMatchers("/incidents/**").authenticated()

                        // Digər bütün requestlər
                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}