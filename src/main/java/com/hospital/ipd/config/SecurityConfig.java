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

    /**
     * 1) PasswordEncoder bean for hashing and verifying passwords.
     */
    @Bean

    public PasswordEncoder passwordEncoder() {
        // Explicitly set strength to 10 to match $2a$10$ hashes
        return new BCryptPasswordEncoder(10);
    }

    /**
     * 2) Main security filter chain.
     *    - wires in our custom AuthenticationProvider
     *    - disables CSRF for simplicity
     *    - configures URL authorization rules
     *    - sets up form-login and logout
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            IpDAuthenticationProvider authProvider
    ) throws Exception {

        http
                // use our custom authentication logic (employees & patients)
                .authenticationProvider(authProvider)

                // disable CSRF for simplicity (enable if you need CSRF protection)
                .csrf().disable()

                // URL authorization
                .authorizeHttpRequests(auth -> auth
                        // static assets (CSS, JS, images) are public
                        .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()

                        // login and signup pages must be accessible without authentication
                        .requestMatchers("/login", "/signup").permitAll()

                        // admin-only endpoints
                        .requestMatchers(
                                "/admin/**",
                                "/viewAllEmployees",
                                "/adminDashboard",
                                "/manageRequestOptions",
                                "/addRequestOption",
                                "/updateRequestOptionForm",
                                "/updateRequestOption",
                                "/deleteRequestOption" )
                        .hasRole("ADMIN")

                        // medical staff & admin endpoints
                        .requestMatchers(
                                "/viewTask",
                                "/startTask",
                                "/completeTask",
                                "/updateEmployeeStatus"
                        ).hasAnyRole("NURSE", "DOCTOR", "HOUSEKEEPING")

                        /// admin and medical staff
                        .requestMatchers( "/employeeTasksDashboard").hasAnyRole( "NURSE", "ADMIN", "DOCTOR", "HOUSEKEEPING")

                        // patient & nurse endpoints
                        .requestMatchers("/requestOptions", "/requestHelp")
                        .hasRole("PATIENT")

                        // any other request requires authentication
                        .anyRequest().authenticated()
                )

                // form-login configuration
                .formLogin(form -> form
                        .loginPage("/login")       // custom login page
                        .usernameParameter("email") // ← bind Spring’s “username” to your “email” field
                        .passwordParameter("password")
                        .defaultSuccessUrl("/postLogin", true) // landing page after login
                        .permitAll()
                )

                // logout configuration
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
}
