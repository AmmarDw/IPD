package com.hospital.ipd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Returns all employees, optionally filtering by a set of role IDs
     * and/or status (1 = working, 0 = off-duty), sorted by employeeId.
     */
    public List<Employee> findFiltered(List<Integer> roleIds, Integer status) {


        boolean hasRoleFilter = roleIds != null && !roleIds.isEmpty();
        boolean hasStatusFilter = status != null;
        boolean statusBool = hasStatusFilter && status == 1;

        if (!hasRoleFilter && !hasStatusFilter) {
            return employeeRepository.findAllByOrderByEmployeeId();
        }
        if (!hasRoleFilter) {
            return employeeRepository.findByStatusOrderByEmployeeId(statusBool);
        }
        if (!hasStatusFilter) {
            return employeeRepository.findByRole_RoleIdInOrderByEmployeeId(roleIds);
        }
        return employeeRepository.findByRole_RoleIdInAndStatusOrderByEmployeeId(roleIds, statusBool);
    }

    /**
     * Fetch a single employee by their numeric ID.
     * Added to support DashboardController.findById usage.
     */
    public Employee findById(Integer employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    /**
     * Fetch a single employee by their email.
     * Added to support DashboardController.findByEmail usage.
     */
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    /**
     * Legacy method retaining semantic clarity.
     */
    public Employee findEmployee(Integer employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    /**
     * Alternate lookup by email.
     */
    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    /**
     * Updates the status of an employee (e.g., active/inactive).
     * @param employeeId ID of the employee to update.
     * @param newStatus New status value (true for active, false for inactive).
     * @return Updated Employee object, or null if employee not found.
     */
    public Employee updateStatus(Integer employeeId, boolean newStatus) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return null;
        }
        employee.setStatus(newStatus);
        return employeeRepository.save(employee);
    }

}
