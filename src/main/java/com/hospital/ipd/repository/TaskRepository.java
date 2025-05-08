package com.hospital.ipd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.ipd.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // for “Pending” tasks
    List<Task> findByAssignedToEmployeeIdAndInProgressFalseAndStatusFalse(Integer employeeId);

    // for “In-Progress” tasks
    List<Task> findByAssignedToEmployeeIdAndInProgressTrueAndStatusFalse(Integer employeeId);

    // for “Completed” tasks
    List<Task> findByAssignedToEmployeeIdAndStatusTrue(Integer employeeId);

    public List<Task> findByAssignedToEmployeeId(Integer employeeId);    // All
}
