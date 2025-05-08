package com.hospital.ipd.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "option_id")
    private RequestOption requestOption;

    /** when the patient requested it */
    private LocalDateTime timestamp;

    /** when the employee clicked “Start” */
    private LocalDateTime startedAt;

    /** true if work is in progress */
    private boolean inProgress;

    /** true if work is completed */
    private boolean status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee assignedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient requestedBy;

    // /** (set on completion) */
    // @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "completed_by_id")
    // private Employee completedBy;

    /** when the task was completed */
    private LocalDateTime completedAt;

    // Lombok @Getter/@Setter generate all necessary getters and setters
}
