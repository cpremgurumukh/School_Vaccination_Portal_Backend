package com.prem.vaccinationportal.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prem.vaccinationportal.model.VaccinationDrive;
import com.prem.vaccinationportal.service.VaccinationDriveService;

import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class VaccinationDriveController {
    @Autowired
    private VaccinationDriveService driveService;

    @PostMapping
    public VaccinationDrive createDrive(@RequestBody VaccinationDrive drive) {
        return driveService.createDrive(drive);
    }

    @PutMapping("/{id}")
    public VaccinationDrive updateDrive(@PathVariable Long id, @RequestBody VaccinationDrive drive) {
        return driveService.updateDrive(id, drive);
    }

    @DeleteMapping("/{id}")
    public void deleteDrive(@PathVariable Long id) {
        driveService.deleteDrive(id);
    }

    @GetMapping("/upcoming")
    public List<VaccinationDrive> getUpcomingDrives() {
        return driveService.getUpcomingDrives();
    }
}
