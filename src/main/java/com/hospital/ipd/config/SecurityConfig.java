package com.hospital.ipd.config;

import com.hospital.ipd.security.IpDAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            IpDAuthenticationProvider authProvider
    ) throws Exception {
        http
                .authenticationProvider(authProvider)
                .csrf(csrf -> csrf.disable()) // More modern syntax

                .authorizeHttpRequests(auth -> auth
                        // Public assets
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // Public pages
                        .requestMatchers("/login", "/signup").permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/admin/**",
                                "/viewAllEmployees",
                                "/adminDashboard",
                                "/manageRequestOptions/**"
                        ).hasRole("ADMIN")

                        // Employee tasks dashboard - now accessible by both admin and medical staff
                        .requestMatchers("/employeeTasksDashboard").hasAnyRole("ADMIN", "NURSE", "DOCTOR", "HOUSEKEEPING")

                        // Medical staff endpoints
                        .requestMatchers(
                                "/viewTask",
                                "/startTask",
                                "/completeTask"
                        ).hasAnyRole("NURSE", "DOCTOR", "HOUSEKEEPING")

                        // Patient endpoints
                        .requestMatchers("/requestOptions", "/requestHelp").hasRole("PATIENT")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/postLogin", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
}
