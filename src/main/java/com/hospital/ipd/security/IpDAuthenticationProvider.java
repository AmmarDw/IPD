package com.hospital.ipd.security;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Patient;
import com.hospital.ipd.repository.EmployeeRepository;
import com.hospital.ipd.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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

        Employee e = empRepo.findByEmail(email);
        if (e!=null && encoder.matches(pwd,e.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    email, null,
                    List.of(new SimpleGrantedAuthority("ROLE_"+e.getRole().getRoleName()))
            );
        }
        Patient p = patRepo.findByEmail(email);
        if (p!=null && encoder.matches(pwd,p.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    email, null,
                    List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
            );
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.equals(aClass);
    }
}
