package com.prem.vaccinationportal.config;

import com.prem.vaccinationportal.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.setAllowedOrigins(java.util.Arrays.asList("http://localhost:3000", "http://localhost:4200"));
                corsConfiguration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(java.util.Arrays.asList("*"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/login", "/api/register").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(httpBasic -> httpBasic.realmName("VaccinationPortal"));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            com.prem.vaccinationportal.model.User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
