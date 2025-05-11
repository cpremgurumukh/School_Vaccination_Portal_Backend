package com.prem.vaccinationportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.vaccinationportal.model.VaccinationRecord;

public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
}
