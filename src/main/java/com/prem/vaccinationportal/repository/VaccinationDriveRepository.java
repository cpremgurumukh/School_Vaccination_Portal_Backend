package com.prem.vaccinationportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.vaccinationportal.model.VaccinationDrive;

import java.time.LocalDate;
import java.util.List;

public interface VaccinationDriveRepository extends JpaRepository<VaccinationDrive, Long> {
    List<VaccinationDrive> findByDateBetween(LocalDate start, LocalDate end);
    List<VaccinationDrive> findByDate(LocalDate date);
}
