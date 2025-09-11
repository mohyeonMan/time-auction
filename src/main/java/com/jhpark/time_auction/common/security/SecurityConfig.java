// package com.jhpark.time_auction.common.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class SecurityConfig {
//     @Bean
//     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.csrf(csrf -> csrf.disable());
//         http.headers(h -> h.frameOptions(f -> f.sameOrigin()));
//         http.authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/ws/**").permitAll()
//                 .anyRequest().permitAll()
//         );
//         return http.build();
//     }
// }