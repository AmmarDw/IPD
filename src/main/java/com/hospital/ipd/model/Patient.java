package com.hospital.ipd.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer patientId;


    String name, email, password, room, bed;

    String phone; //  Changed from int to String — for same reasons as Employee

    @OneToMany(mappedBy="requestedBy")
    List<Task> requests;
}