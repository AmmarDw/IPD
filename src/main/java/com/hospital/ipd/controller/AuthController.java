package com.hospital.ipd.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Patient;
import com.hospital.ipd.model.Role;
import com.hospital.ipd.repository.EmployeeRepository;
import com.hospital.ipd.repository.PatientRepository;
import com.hospital.ipd.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PatientRepository patRepo;

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private RoleService roleService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/postLogin")
    public String postLoginRedirect(Authentication auth) {
        if (auth == null || auth.getAuthorities() == null) {
            System.out.println("[DEBUG] - No authentication found or roles missing.");
            return "redirect:/login?error";
        }

        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();
        System.out.println("[DEBUG] - Logged in with roles: " + roles);

        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/adminDashboard";
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_NURSE")) ||
            roles.contains(new SimpleGrantedAuthority("ROLE_DOCTOR")) ||
            roles.contains(new SimpleGrantedAuthority("ROLE_HOUSEKEEPING"))) {
            return "redirect:/employeeTasksDashboard";
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            return "redirect:/requestOptions";
        }

        System.out.println("[DEBUG] - No matching role found. Redirecting to login.");
        return "redirect:/login?error";
    }

@GetMapping("/signup")
public String showSignupForm() {
    return "signup";
}

@PostMapping("/signup")
public String signup(HttpServletRequest request) {
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String phone = request.getParameter("phone");
    String userType = request.getParameter("userType");
    String salaryStr = request.getParameter("salary");

    if ("PATIENT".equalsIgnoreCase(userType)) {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setEmail(email);
        patient.setPassword(passwordEncoder.encode(password));
        patient.setPhone(phone);
        patient.setRoom("A101");
        patient.setBed("B1");
        patRepo.save(patient);
    } else {
        Role role = roleService.getRoleByName(userType);

        if (role == null) {
            System.out.println("[ERROR]  Invalid role type: " + userType);
            return "redirect:/signup?error=invalid_role";
        }

        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setPhone(phone);
        employee.setStatus(true); //  Use boolean true
        employee.setSalary(Double.parseDouble(salaryStr));
        employee.setRole(role);
        empRepo.save(employee);
    }

    return "redirect:/login";
}
}
