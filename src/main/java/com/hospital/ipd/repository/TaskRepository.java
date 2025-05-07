// In com.hospital.ipd.repository package
package com.hospital.ipd.repository;

import com.hospital.ipd.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByAssignedToEmployeeId(Integer employeeId);
}