package com.hospital.ipd.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    /**
     * Shows the Admin Dashboard page,
     * with links to Manage Request Options and Monitor Employees.
     */
    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model, HttpServletRequest request) {
        // ← add currentUri
        model.addAttribute("currentUri", request.getRequestURI());
        return "adminDashboard";
    }
}