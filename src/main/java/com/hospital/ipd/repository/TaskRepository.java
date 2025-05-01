package com.hospital.ipd.repository;

import com.hospital.ipd.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    // go to gbt, give him the task and employee models. Ask for a select statement by assignedTo
}
