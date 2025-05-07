// In com.hospital.ipd.service package
package com.hospital.ipd.service;

import com.hospital.ipd.model.Task;
import com.hospital.ipd.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Transactional
    public List<Task> employeeTasks(Integer employeeId) {
        return taskRepository.findByAssignedToEmployeeId(employeeId);
    }

    // Add other task-related methods as needed

}