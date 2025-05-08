package com.hospital.ipd.controller;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Role;
import com.hospital.ipd.service.EmployeeService;
import com.hospital.ipd.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;


    /**
     * Show the "view all employees" page, with optional filters.
     */
    @GetMapping("/viewAllEmployees")
    public String viewAllEmployees(
            // receive raw String values from the checkboxes
            @RequestParam(required = false, name = "roleIds") List<String> rawRoleIds,
            @RequestParam(required = false) Integer status,
            Model model,
            HttpServletRequest request
    ) {
        // Convert rawRoleIds to List<Integer>, unless "ALL" was selected (or nothing),
        // in which case we leave it null to mean "no filter"
        List<Integer> roleIds = null;
        if (rawRoleIds != null && !rawRoleIds.contains("ALL")) {
            roleIds = rawRoleIds.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }

        // fetch roles (excluding ADMIN)
        List<Role> roles = roleService.getAllRolesExcludingAdmin();

        // fetch employees with the same filter logic you wrote
        List<Employee> employees = employeeService.findFiltered(roleIds, status);

        // expose to Thymeleaf
        model.addAttribute("roles", roles);
        model.addAttribute("employees", employees);
        // pass our computed List<Integer> (or null) so your checkboxes and "All roles" logic still works
        model.addAttribute("roleIds", roleIds);
        model.addAttribute("status", status);

        // give your template the current URI so you can do the active-link highlighting
        model.addAttribute("currentUri", request.getRequestURI());

        return "viewAllEmployees";
    }
}