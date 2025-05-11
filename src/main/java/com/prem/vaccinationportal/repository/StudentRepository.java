package com.prem.vaccinationportal.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prem.vaccinationportal.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentId(String studentId);

    @Query("SELECT DISTINCT s FROM Student s JOIN s.vaccinationRecords vr")
    List<Student> findStudentsWithVaccinations();

    @Query("SELECT DISTINCT s FROM Student s JOIN s.vaccinationRecords vr")
    Page<Student> findAllWithVaccinations(Pageable pageable);

    @Query("SELECT DISTINCT s FROM Student s JOIN s.vaccinationRecords vr WHERE vr.vaccineName = :vaccineName")
    Page<Student> findByVaccinationRecordsVaccineName(@Param("vaccineName") String vaccineName, Pageable pageable);
}