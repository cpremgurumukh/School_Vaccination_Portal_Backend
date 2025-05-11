package com.prem.vaccinationportal.controller;

import com.prem.vaccinationportal.model.Student;
import com.prem.vaccinationportal.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String classGrade,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) String vaccineName) {
        return ResponseEntity.ok(studentService.searchStudents(name, classGrade, studentId, vaccineName));
    }

    @PostMapping("/mark-vaccinated")
    public ResponseEntity<Student> markVaccinated(
            @RequestParam Long studentId,
            @RequestParam Long driveId,
            @RequestParam String vaccineName) {
        return ResponseEntity.ok(studentService.markVaccinated(studentId, driveId, vaccineName));
    }

    @PostMapping("/vaccinate")
    public ResponseEntity<Student> vaccinateStudent(
            @RequestParam Long studentId,
            @RequestParam Long driveId,
            @RequestParam String vaccineName) {
        return ResponseEntity.ok(studentService.vaccinateStudent(studentId, driveId, vaccineName));
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<List<Student>> bulkImport(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(studentService.bulkImport(file));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
}