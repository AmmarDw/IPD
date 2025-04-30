package com.hospital.ipd.controller;

import com.hospital.ipd.model.PriorityEnum;
import com.hospital.ipd.model.RequestOption;
import com.hospital.ipd.model.Role;
import com.hospital.ipd.service.RequestOptionService;
import com.hospital.ipd.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HelpRequestController {

    @Autowired
    private RequestOptionService requestOptionService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/manageRequestOptions")
    public String manageRequestOptions(
            @RequestParam(name = "priority", required = false) List<String> rawPriorities,
            @RequestParam(name = "roleIds", required = false) List<String> rawRoleIds,
            Model model,
            HttpServletRequest request
    ) {
        // build List<PriorityEnum> or leave null for "ALL"
        List<PriorityEnum> selectedPriorities = null;
        if (rawPriorities != null && !rawPriorities.contains("ALL")) {
            selectedPriorities = rawPriorities.stream()
                    .map(PriorityEnum::valueOf)
                    .collect(Collectors.toList());
        }

        // build List<Integer> or leave null for "ALL"
        List<Integer> selectedRoles = null;
        if (rawRoleIds != null && !rawRoleIds.contains("ALL")) {
            selectedRoles = rawRoleIds.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }

        // fetch filtered options
        List<RequestOption> options = requestOptionService.findFiltered(selectedPriorities, selectedRoles);

        // data for your filter controls
        List<PriorityEnum> allPriorities = Arrays.asList(PriorityEnum.values());
        List<Role> allRoles = roleService.getAllRolesExcludingAdmin();

        model.addAttribute("options", options);
        model.addAttribute("priorities", allPriorities);
        model.addAttribute("roles", allRoles);
        model.addAttribute("selectedPriorities", selectedPriorities);
        model.addAttribute("selectedRoles", selectedRoles);
        model.addAttribute("currentUri", request.getRequestURI());

        return "manageRequestOptions";
    }

    @PostMapping("/addRequestOption")
    public String addRequestOption(
            @RequestParam String description,
            @RequestParam String priority,
            @RequestParam int responsibleRole,
            Model model
    ) {
        Role role = roleService.getRoleById(responsibleRole);
        RequestOption newRequestOption = new RequestOption();
        newRequestOption.setDescription(description);
        newRequestOption.setPriority(PriorityEnum.valueOf(priority));
        newRequestOption.setResponsibleRole(role);
        requestOptionService.save(newRequestOption);
        return "redirect:/manageRequestOptions";
    }

    // Get the option to be updated, and pre-fill the modal form
    @GetMapping("/updateRequestOptionForm")
    public String updateRequestOptionForm(@RequestParam("optionId") int optionId, Model model) {
        // Retrieve the request option by ID
        RequestOption requestOption = requestOptionService.getRequestOptionById(optionId);

        // If the option is found, add it to the model
        if (requestOption != null) {
            model.addAttribute("option", requestOption);
            // Fetch all roles to display in the dropdown for updating the responsible role
            List<Role> roles = roleService.getAllRolesExcludingAdmin(); // Fetch all roles from the role service
            model.addAttribute("roles", roles); // Add the roles to the model
        } else {
            // Option not found, handle this case appropriately (maybe redirect to a 404 page or show an error message)
            return "redirect:/manageRequestOptions";
        }

        // Return the same page with the option and roles data for updating
        return "manageRequestOptions";  // The same page but now with the option data for update
    }



    // Handle the update request
    @PostMapping("/updateRequestOption")
    public String updateRequestOption(
            @RequestParam int optionId,
            @RequestParam String description,
            @RequestParam String priority,
            @RequestParam int responsibleRole,
            Model model
    ) {
        // Fetch the option by ID
        RequestOption optionToUpdate = requestOptionService.getRequestOptionById(optionId);

        if (optionToUpdate != null) {
            // Update the option fields
            optionToUpdate.setDescription(description);
            optionToUpdate.setPriority(PriorityEnum.valueOf(priority));
            Role role = roleService.getRoleById(responsibleRole);
            optionToUpdate.setResponsibleRole(role);

            // Save the updated option
            requestOptionService.save(optionToUpdate);
        }

        // Redirect to manageRequestOptions after update
        return "redirect:/manageRequestOptions";
    }

    // DELETE method for deleting a request option
    @PostMapping("/deleteRequestOption")
    public String deleteRequestOption(
            @RequestParam int optionId
    ) {
        requestOptionService.deleteById(optionId);
        return "redirect:/manageRequestOptions";
    }
}