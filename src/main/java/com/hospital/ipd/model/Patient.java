package com.hospital.ipd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer patientId;


    String name, email, password, room, bed;

    int phone;

    @OneToMany(mappedBy="requestedBy")
    List<Task> requests;
}
