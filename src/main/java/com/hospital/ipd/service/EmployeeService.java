package com.hospital.ipd.service;

import com.hospital.ipd.model.Employee;
import com.hospital.ipd.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;


    /**
     * Returns all employees, optionally filtering by a set of role IDs
     * and/or status (1 = working, 0 = off-duty), sorted by employeeId.
     *
     * @param roleIds list of role IDs to include (null or empty → all roles)
     * @param status  1 for working, 0 for off-duty, null → both
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

    // TODO Salem task
    // create findEmployee that calls the EmployeeRepository proper function
    // - maybe findByEmployeeId or getEmployeeByEmployeeId ask gbt for it


    // Salem task - find employee by ID (updated to use Integer instead of Long)
    public Employee findEmployee(Integer employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    // Additional method needed for DashboardController
    public Employee findEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }


}
