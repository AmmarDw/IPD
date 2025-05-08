package com.hospital.ipd.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Patient;
import com.hospital.ipd.model.PriorityEnum;
import com.hospital.ipd.model.RequestOption;
import com.hospital.ipd.model.Role;
import com.hospital.ipd.model.Task;
import com.hospital.ipd.repository.EmployeeRepository;
import com.hospital.ipd.repository.PatientRepository;
import com.hospital.ipd.repository.TaskRepository;
import com.hospital.ipd.service.RequestOptionService;
import com.hospital.ipd.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HelpRequestController {

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private RequestOptionService requestOptionService;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private RoleService roleService;

    // ------------------------------------------------
    // Admin: List & filter all RequestOptions
    // ------------------------------------------------
    @GetMapping("/manageRequestOptions")
    public String manageRequestOptions(
            @RequestParam(name = "priority", required = false) List<String> rawPriorities,
            @RequestParam(name = "roleIds", required = false)     List<String> rawRoleIds,
            Model model,
            HttpServletRequest request
    ) {
        // build Priority filter or null = “ALL”
        List<PriorityEnum> selectedPriorities = null;
        if (rawPriorities != null && !rawPriorities.contains("ALL")) {
            selectedPriorities = rawPriorities.stream()
                    .map(PriorityEnum::valueOf)
                    .collect(Collectors.toList());
        }

        // build RoleId filter or null = “ALL”
        List<Integer> selectedRoles = null;
        if (rawRoleIds != null && !rawRoleIds.contains("ALL")) {
            selectedRoles = rawRoleIds.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }

        List<RequestOption> options = requestOptionService.findFiltered(selectedPriorities, selectedRoles);
        model.addAttribute("options", options);
        model.addAttribute("priorities", Arrays.asList(PriorityEnum.values()));
        model.addAttribute("roles", roleService.getAllRolesExcludingAdmin());
        model.addAttribute("selectedPriorities", selectedPriorities);
        model.addAttribute("selectedRoles", selectedRoles);
        model.addAttribute("currentUri", request.getRequestURI());

        return "manageRequestOptions";
    }

    // ------------------------------------------------
    // Admin: Create, update, delete RequestOptions
    // ------------------------------------------------
    @PostMapping("/addRequestOption")
    public String addRequestOption(
            @RequestParam String description,
            @RequestParam String priority,
            @RequestParam int responsibleRole
    ) {
        Role role = roleService.getRoleById(responsibleRole);
        RequestOption option = new RequestOption();
        option.setDescription(description);
        option.setPriority(PriorityEnum.valueOf(priority));
        option.setResponsibleRole(role);
        requestOptionService.save(option);
        return "redirect:/manageRequestOptions";
    }

    @GetMapping("/updateRequestOptionForm")
    public String updateRequestOptionForm(@RequestParam("optionId") int optionId, Model model) {
        RequestOption option = requestOptionService.getRequestOptionById(optionId);
        if (option == null) {
            return "redirect:/manageRequestOptions";
        }
        model.addAttribute("option", option);
        model.addAttribute("roles", roleService.getAllRolesExcludingAdmin());
        return "manageRequestOptions";
    }

    @PostMapping("/updateRequestOption")
    public String updateRequestOption(
            @RequestParam int optionId,
            @RequestParam String description,
            @RequestParam String priority,
            @RequestParam int responsibleRole
    ) {
        RequestOption option = requestOptionService.getRequestOptionById(optionId);
        if (option != null) {
            option.setDescription(description);
            option.setPriority(PriorityEnum.valueOf(priority));
            option.setResponsibleRole(roleService.getRoleById(responsibleRole));
            requestOptionService.save(option);
        }
        return "redirect:/manageRequestOptions";
    }

    @PostMapping("/deleteRequestOption")
    public String deleteRequestOption(@RequestParam int optionId) {
        requestOptionService.deleteById(optionId);
        return "redirect:/manageRequestOptions";
    }

    // ------------------------------------------------
    // Patient: View available help options
    // ------------------------------------------------
    @GetMapping("/requestOptions")
    public String viewRequestOptions(Model model) {
        model.addAttribute("options", requestOptionService.findAllOptions());
        return "requestOptions";
    }

    // ------------------------------------------------
    // Patient: Submit help request → auto-assign task
    // ------------------------------------------------
    @PostMapping("/requestHelp")
    public String requestHelp(
            @RequestParam("optionId") int optionId,
            Authentication auth
    ) {
        // 1) Find patient
        String email = auth.getName();
        Patient patient = patientRepo.findByEmail(email);
        if (patient == null) {
            return "redirect:/login?error=patient_not_found";
        }

        // 2) Find chosen RequestOption
        RequestOption option = requestOptionService.getRequestOptionById(optionId);
        if (option == null) {
            return "redirect:/requestOptions?error=option_not_found";
        }

        // 3) Fetch active employees matching required role
        List<Employee> candidates = empRepo.findByRoleAndStatus(option.getResponsibleRole(), true);
        if (candidates.isEmpty()) {
            return "redirect:/requestOptions?error=no_available_employees";
        }

        // 4) Calculate “busy score” (higher = more work)
        Map<Employee,Integer> busyScores = new HashMap<>();
        for (Employee emp : candidates) {
            int score = 0;
            if (emp.getTasks() != null) {
                for (Task t : emp.getTasks()) {
                    if (!t.isStatus()) {
                        switch (t.getRequestOption().getPriority()) {
                            case CRITICAL -> score += 4;
                            case HIGH     -> score += 3;
                            case MEDIUM   -> score += 2;
                            case LOW      -> score += 1;
                        }
                    }
                }
            }
            busyScores.put(emp, score);
        }

        // 5) Pick the least-busy
        Employee assignee = Collections.min(
            busyScores.entrySet(),
            Comparator.comparingInt(Map.Entry::getValue)
        ).getKey();

        // 6) Create + save Task
        Task task = new Task();
        task.setRequestOption(option);
        task.setRequestedBy(patient);
        task.setAssignedTo(assignee);
        task.setTimestamp(LocalDateTime.now());
        task.setStatus(false);
        taskRepo.save(task);

        return "redirect:/requestOptions?success";
    }
}
