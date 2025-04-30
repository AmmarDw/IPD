package com.hospital.ipd.service;

import com.hospital.ipd.model.Role;
import com.hospital.ipd.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Fetches all roles except "ADMIN"
     */
    public List<Role> getAllRolesExcludingAdmin() {
        return roleRepository.findAllByRoleNameNot("ADMIN");
    }

    /**
     * Fetches a role by its ID.
     *
     * @param roleId the ID of the role to be fetched
     * @return the Role if found, otherwise returns null
     */
    public Role getRoleById(int roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        return role.orElse(null);
    }
}
