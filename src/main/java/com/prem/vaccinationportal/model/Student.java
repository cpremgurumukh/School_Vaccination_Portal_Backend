package com.prem.vaccinationportal.model;

import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "class_grade", nullable = false)
    private String classGrade;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VaccinationRecord> vaccinationRecords = new ArrayList<>();
}