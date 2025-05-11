package com.prem.vaccinationportal.service;

import com.prem.vaccinationportal.repository.StudentRepository;
import com.prem.vaccinationportal.repository.VaccinationDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private VaccinationDriveRepository driveRepository;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        long totalStudents = studentRepository.count();
        long vaccinatedStudents = studentRepository.findStudentsWithVaccinations().size();
        double vaccinatedPercentage = totalStudents > 0 ? (vaccinatedStudents * 100.0 / totalStudents) : 0;

        data.put("totalStudents", totalStudents);
        data.put("vaccinatedStudents", vaccinatedStudents);
        data.put("vaccinatedPercentage", vaccinatedPercentage);
        data.put("upcomingDrives", driveRepository.findByDateBetween(
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        ));

        return data;
    }
}