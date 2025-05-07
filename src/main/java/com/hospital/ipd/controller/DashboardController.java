package com.hospital.ipd.controller;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Task;
import com.hospital.ipd.service.EmployeeService;
import com.hospital.ipd.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private TaskService taskService;


    @Autowired
    private EmployeeService employeeService;

    /**
     * Shows the Admin Dashboard page.
     */
    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "adminDashboard";
    }

    /**
     * Shows the Employee Tasks Dashboard page.
     */
    @GetMapping("/employeeTasksDashboard")
    public String employeeTasksDashboard(
            @RequestParam(required = false, name = "employeeId") Integer employeeId,
            Authentication auth,
            Model model) {

        {
            try {
                // Existing logic here
            } catch (Exception e) {
                model.addAttribute("error", "Failed to load tasks: " + e.getMessage());
                return "error-page"; // Create a simple error.html template
            }
        }

        // Get current user's role
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // For non-admins, force their own employee ID
        if (!isAdmin) {
            Employee currentEmployee = employeeService.findEmployeeByEmail(auth.getName());
            employeeId = currentEmployee.getEmployeeId();
        }

        // Get tasks and employee info
        List<Task> tasks = taskService.employeeTasks(employeeId);
        Employee employee = employeeService.findEmployee(employeeId);

        // Add to model
        model.addAttribute("tasks", tasks);
        model.addAttribute("employee", employee);

        return "employeeTasksDashboard";
    }
}
