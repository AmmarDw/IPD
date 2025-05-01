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

    // TODO Salem task
    // create @GetMapping("/employeeTasksDashboard")
    // should take an employeeId parameter @RequestParam(required = false, name = "employeeId")
    // should take an authentication parameter Authentication auth
    // refer to AuthController to check how - part of - ROLE based access is implemented
    // call employeeTasks(id) from the TaskService. create autowired taskService object
    // if ROLE is employee get his id from the auth as well. or mostly you should take his id from the session
    // add tasks returned from the service to the page through (model.addAttribute)
    // now we need to get the employee object from the EmployeeService. add the employee object through (model.addAttribute)
    // now create the html page employeeTasksDashboard.html that contains the employee name, role, and status at the top
    // and the tasks table under it. give gbt the viewAllEmployees.html and manageRequestOptions.html pages to generate the required table that has the same styling as in those pages
}