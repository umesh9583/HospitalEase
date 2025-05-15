package com.hms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // Permit public access to static resources and HTML pages
                .requestMatchers("/", "/index.html", "/login.html", "/register.html", 
                                 "/contact.html", "/css/**", "/js/**").permitAll()
                // Permit public access to registration and login endpoints
                .requestMatchers("/form-login", "/registerPatient").permitAll()
                // Permit access to dashboard HTML files after login (but API endpoints will still be protected)
                .requestMatchers("/patient-dashboard.html", "/doctor-dashboard.html", 
                                 "/admin-dashboard.html", "/nurse-dashboard.html", 
                                 "/accountant-dashboard.html").permitAll()
                // Protect API endpoints by role
                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/nurse/**").hasRole("NURSE")
                .requestMatchers("/api/accountant/**").hasRole("ACCOUNTANT")
                .anyRequest().authenticated()
            )
            // Disable default form login since we're using a custom endpoint
            .formLogin().disable()
            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler(new SecurityContextLogoutHandler())
                .logoutSuccessUrl("/login.html?logout")
                .permitAll()
            )
            // Ensure session is stateless to avoid conflicts
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login.html?expired")
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}