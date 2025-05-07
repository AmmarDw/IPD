package com.hospital.ipd.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Patient;
import com.hospital.ipd.repository.EmployeeRepository;
import com.hospital.ipd.repository.PatientRepository;

@Component
// @Profile("prod")
public class IpDAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    EmployeeRepository empRepo;
    @Autowired
    PatientRepository patRepo;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String email = auth.getName();
        String pwd = auth.getCredentials().toString();

        System.out.println("[DEBUG] Trying to authenticate: " + email);

        Employee e = empRepo.findByEmail(email);
        if (e != null) {
            System.out.println("[DEBUG] Found employee: " + e.getEmail());
            System.out.println("[DEBUG] Stored hash: " + e.getPassword());
            boolean passwordMatch = encoder.matches(pwd, e.getPassword());
            System.out.println("[DEBUG] Password match (employee): " + passwordMatch);

            if (passwordMatch) {
                System.out.println("[DEBUG] Authentication success as employee, role: " + e.getRole().getRoleName());
                return new UsernamePasswordAuthenticationToken(
                        email, null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + e.getRole().getRoleName()))
                );
            }
        }

        Patient p = patRepo.findByEmail(email);
        if (p != null) {
            System.out.println("[DEBUG] Found patient: " + p.getEmail());
            System.out.println("[DEBUG] Stored hash: " + p.getPassword());
            boolean passwordMatch = encoder.matches(pwd, p.getPassword());
            System.out.println("[DEBUG] Password match (patient): " + passwordMatch);

            if (passwordMatch) {
                System.out.println("[DEBUG] Authentication success as patient.");
                return new UsernamePasswordAuthenticationToken(
                        email, null,
                        List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
                );
            }
        }

        System.out.println("[DEBUG] Authentication failed for: " + email);
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
