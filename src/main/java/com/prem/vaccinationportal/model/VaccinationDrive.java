package com.prem.vaccinationportal.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Entity
@Table(name = "vaccination_drives")
public class VaccinationDrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @JsonProperty("driveDate")
    private LocalDate date;

    @Column(name = "available_doses", nullable = false)
    private int availableDoses;

    @ElementCollection
    @CollectionTable(name = "drive_applicable_classes", joinColumns = @JoinColumn(name = "drive_id"))
    @Column(name = "class_grade")
    private List<String> applicableClasses;

    @Column(nullable = false)
    private boolean completed;
}
