package com.hospital.ipd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer taskId;

    @ManyToOne
    @JoinColumn(name="option_id")
    RequestOption requestOption;

    LocalDateTime timestamp;

    boolean status;

    @ManyToOne
    @JoinColumn(name="employee_id")
    Employee assignedTo;

    @ManyToOne
    @JoinColumn(name="patient_id")
    Patient requestedBy;
}