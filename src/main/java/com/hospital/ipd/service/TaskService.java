package com.hospital.ipd.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.model.Patient;
import com.hospital.ipd.model.RequestOption;
import com.hospital.ipd.model.Task;
import com.hospital.ipd.repository.EmployeeRepository;
import com.hospital.ipd.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired private TaskRepository taskRepo;
    @Autowired private EmployeeRepository empRepo;

    // simple fetch-all for an employee
    public List<Task> employeeTasks(Integer employeeId) {
        return taskRepo.findByAssignedToEmployeeId(employeeId);
    }

    // buckets
    public List<Task> getPendingTasks(Integer employeeId) {
        return taskRepo.findByAssignedToEmployeeIdAndInProgressFalseAndStatusFalse(employeeId);
    }
    public List<Task> getInProgressTasks(Integer employeeId) {
        return taskRepo.findByAssignedToEmployeeIdAndInProgressTrueAndStatusFalse(employeeId);
    }
    public List<Task> getCompletedTasks(Integer employeeId) {
        return taskRepo.findByAssignedToEmployeeIdAndStatusTrue(employeeId);
    }

    @Transactional
    public void startTask(int taskId) {
        Task t = taskRepo.findById(taskId).orElseThrow();
        t.setStartedAt(LocalDateTime.now());
        t.setInProgress(true);
        taskRepo.save(t);
    }

    @Transactional
    public void completeTask(int taskId, String finisherEmail) {
        Task t = taskRepo.findById(taskId).orElseThrow();
        Employee finisher = empRepo.findByEmail(finisherEmail);
        // t.setCompletedBy(finisher);
        t.setCompletedAt(LocalDateTime.now());
        t.setStatus(true);
        t.setInProgress(false);
        taskRepo.save(t);
    }

    /** 
     * Your “least-busy” assignment algorithm 
     * (you could also move the HelpRequestController logic here).
     */
    @Transactional
    public Task assignNewTask(Patient patient, RequestOption option) {
        // ... same as you have in HelpRequestController ...
        // return saved Task
        throw new UnsupportedOperationException("…");
    }
}
