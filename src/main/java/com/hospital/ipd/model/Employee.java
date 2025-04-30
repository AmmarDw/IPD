package com.hospital.ipd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer employeeId;

    String name, email, password;

    int phone;

    double salary;

    @ManyToOne
    @JoinColumn(name="role_id")
    Role role;

    boolean status;

    @OneToMany(mappedBy="assignedTo")
    List<Task> tasks;
}
