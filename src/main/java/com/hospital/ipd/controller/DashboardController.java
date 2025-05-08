package com.hospital.ipd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Task;
import com.hospital.ipd.service.EmployeeService;
import com.hospital.ipd.service.TaskService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class DashboardController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Admin landing page.
     */
    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model, HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        return "adminDashboard";
    }

    /**
     * Employee Tasks Dashboard.
     * - Admins may pass ?employeeId=123
     * - Non-admins always see their own queue.
     * Also reads an optional flash‐attribute "message" for notifications.
     */
    @GetMapping("/employeeTasksDashboard")
    public String employeeTasksDashboard(
            @RequestParam(name = "employeeId", required = false) Integer employeeId,
            Authentication auth,
            Model model,
            HttpServletRequest request,
            @ModelAttribute("message") String message
    ) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            Employee me = employeeService.findByEmail(auth.getName());
            employeeId = me.getEmployeeId();
        }

        Employee employee = employeeService.findById(employeeId);
        List<Task> pending    = taskService.getPendingTasks(employeeId);
        List<Task> inProgress = taskService.getInProgressTasks(employeeId);
        List<Task> completed  = taskService.getCompletedTasks(employeeId);

        model.addAttribute("currentUri",    request.getRequestURI());
        model.addAttribute("employee",      employee);
        model.addAttribute("pendingTasks",    pending);
        model.addAttribute("inProgressTasks", inProgress);
        model.addAttribute("completedTasks",  completed);

        if (message != null && !message.isBlank()) {
            model.addAttribute("message", message);
        }

        return "employeeTasksDashboard";
    }

    /**
     * Mark a task “started”.
     * Admins get a notification instead of 403.
     */
    @PostMapping("/startTask")
    public String startTask(
            @RequestParam int taskId,
            Authentication auth,
            RedirectAttributes ra
    ) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            ra.addFlashAttribute("message", "Admins cannot start tasks.");
        } else {
            taskService.startTask(taskId);
        }
        return "redirect:/employeeTasksDashboard";
    }

    /**
     * Mark a task “completed”.
     * Admins get a notification instead of 403.
     */
    @PostMapping("/completeTask")
    public String completeTask(
            @RequestParam int taskId,
            Authentication auth,
            RedirectAttributes ra
    ) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            ra.addFlashAttribute("message", "Admins cannot complete tasks.");
        } else {
            taskService.completeTask(taskId, auth.getName());
        }
        return "redirect:/employeeTasksDashboard";
    }
}
