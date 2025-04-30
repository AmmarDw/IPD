package com.hospital.ipd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer roleId;

    String roleName;

    @OneToMany(mappedBy="role")
    List<Employee> employees;
}
