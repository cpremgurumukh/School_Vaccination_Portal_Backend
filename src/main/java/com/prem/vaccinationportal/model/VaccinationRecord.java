package com.prem.vaccinationportal.model;

import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "vaccination_records")
public class VaccinationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vaccine_name", nullable = false)
    private String vaccineName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @ManyToOne
    @JoinColumn(name = "drive_id", nullable = false)
    @JsonIgnore
    private VaccinationDrive drive;
}