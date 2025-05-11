package com.prem.vaccinationportal.service;

import com.prem.vaccinationportal.model.VaccinationDrive;
import com.prem.vaccinationportal.repository.VaccinationDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccinationDriveService {
    @Autowired
    private VaccinationDriveRepository driveRepository;

    public VaccinationDrive createDrive(VaccinationDrive drive) {
        // Validate that date is not null
        if (drive.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Drive date is required");
        }

        // Validate date (at least 15 days in future)
        if (drive.getDate().isBefore(LocalDate.now().plusDays(15))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Drive must be scheduled at least 15 days in advance");
        }

        // Check for scheduling conflicts
        List<VaccinationDrive> existingDrives = driveRepository.findByDate(drive.getDate());
        if (!existingDrives.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A drive is already scheduled for this date");
        }

        drive.setCompleted(false);
        return driveRepository.save(drive);
    }

    public VaccinationDrive updateDrive(Long id, VaccinationDrive drive) {
        VaccinationDrive existing = driveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drive not found"));

        // Cannot edit past or completed drives
        if (existing.isCompleted() || existing.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot edit past or completed drives");
        }

        drive.setId(id);
        return driveRepository.save(drive);
    }

    public void deleteDrive(Long id) {
        VaccinationDrive drive = driveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drive not found"));

        if (drive.getDate().isBefore(LocalDate.now()) || drive.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete past or completed drives");
        }

        driveRepository.deleteById(id);
    }

    public List<VaccinationDrive> getUpcomingDrives() {
        return driveRepository.findByDateBetween(
                LocalDate.now(),
                LocalDate.now().plusDays(30)
        );
    }
}