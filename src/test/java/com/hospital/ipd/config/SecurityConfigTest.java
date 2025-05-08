package com.hospital.ipd.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testPasswordEncoderBean() {
        SecurityConfig config = new SecurityConfig();
        PasswordEncoder encoder = config.passwordEncoder();

        // Assert the type is BCryptPasswordEncoder
        assertTrue(encoder instanceof BCryptPasswordEncoder);

        // Test encoding and matching
        String rawPassword = "securePass123";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword); // Password should be hashed
        assertTrue(encoder.matches(rawPassword, encodedPassword)); // It should match the raw password
    }
}