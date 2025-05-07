package com.hospital.ipd.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer employeeId;

    String name, email, password, phone; // Changed phone from int to String to support formatting, country codes, and leading zeros

    double salary;

    @ManyToOne
    @JoinColumn(name="role_id")
    Role role;

    boolean status; //  Kept as boolean to represent active/inactive status clearly

    @OneToMany(mappedBy="assignedTo")
    List<Task> tasks;
}