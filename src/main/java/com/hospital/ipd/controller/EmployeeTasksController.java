// package com.hospital.ipd.controller;

// import java.time.LocalDateTime;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.*;

// import com.hospital.ipd.model.Employee;
// import com.hospital.ipd.model.Task;
// import com.hospital.ipd.repository.EmployeeRepository;
// import com.hospital.ipd.repository.TaskRepository;

// @Controller
// public class EmployeeTasksController {

//     @Autowired private TaskRepository taskRepo;
//     @Autowired private EmployeeRepository empRepo;

//     /** Show pending / in-progress / completed buckets */
//     @GetMapping("/employeeTasksDashboard")
//     public String dashboard(Authentication auth, Model model) {
//         Employee emp = empRepo.findByEmail(auth.getName());
//         List<Task> pending   = taskRepo.findByAssignedToEmployeeIdAndInProgressFalseAndStatusFalse(emp.getEmployeeId());
//         List<Task> inProgress= taskRepo.findByAssignedToEmployeeIdAndInProgressTrueAndStatusFalse(emp.getEmployeeId());
//         List<Task> completed = taskRepo.findByAssignedToEmployeeIdAndStatusTrue(emp.getEmployeeId());

//         model.addAttribute("employee",     emp);
//         model.addAttribute("pendingTasks", pending);
//         model.addAttribute("inProgressTasks", inProgress);
//         model.addAttribute("completedTasks",  completed);
//         return "employeeTasksDashboard";
//     }

//     /** Mark a task “started” */
//     @PostMapping("/startTask")
//     public String startTask(@RequestParam int taskId) {
//         Task t = taskRepo.findById(taskId).orElseThrow();
//         t.setStartedAt(LocalDateTime.now());
//         t.setInProgress(true);
//         taskRepo.save(t);
//         return "redirect:/employeeTasksDashboard";
//     }

//     /** Mark a task “completed” */
//     @PostMapping("/completeTask")
//     public String completeTask(@RequestParam int taskId, Authentication auth) {
//         Task t = taskRepo.findById(taskId).orElseThrow();
//         Employee finisher = empRepo.findByEmail(auth.getName());
//         t.setStatus(true);
//         t.setInProgress(false);
//         t.setCompletedBy(finisher);
//         t.setCompletedAt(LocalDateTime.now());
//         taskRepo.save(t);
//         return "redirect:/employeeTasksDashboard";
//     }
// }
