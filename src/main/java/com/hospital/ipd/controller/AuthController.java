package com.hospital.ipd.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";   // template: src/main/resources/templates/login.html
    }

    // Spring Security handles POST /login

    /**
     * Role–based post-login landing.
     */
    @GetMapping("/postLogin")
    public String postLoginRedirect(Authentication auth) {
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/adminDashboard";
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_NURSE")) ||
                roles.contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            return "redirect:/employeeTasksDashboard";
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            return "redirect:/requestOptions";
        }

        // fallback: something went wrong
        return "redirect:/login?error";
    }
}