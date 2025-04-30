package com.hospital.ipd.repository;

import com.hospital.ipd.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * All roles whose name is not exactly "ADMIN"
     */
    List<Role> findAllByRoleNameNot(String name);
}
