package com.hospital.ipd.repository;

import com.hospital.ipd.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Employee findByEmail(String email);

    /**
     * No filters, just all employees sorted by ID
     */
    List<Employee> findAllByOrderByEmployeeId();

    Employee findByEmployeeId(int employeeId);


    /**
     * Filter by on-duty status, sorted by ID
     */
    List<Employee> findByStatusOrderByEmployeeId(boolean status);

    /**
     * Filter by one or more role IDs, sorted by ID
     */
    List<Employee> findByRole_RoleIdInOrderByEmployeeId(List<Integer> roleIds);

    /**
     * Filter by both role IDs and status, sorted by ID
     */
    List<Employee> findByRole_RoleIdInAndStatusOrderByEmployeeId(List<Integer> roleIds, boolean status);

}
