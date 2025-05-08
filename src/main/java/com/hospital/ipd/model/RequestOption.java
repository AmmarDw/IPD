package com.hospital.ipd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class RequestOption {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer optionId;

    String description;

    @Enumerated(EnumType.STRING)
    PriorityEnum priority;

    @ManyToOne
    @JoinColumn(name="responsible_role_id")
    Role responsibleRole;

    @OneToMany(mappedBy="requestOption")
    List<Task> tasks;
}
