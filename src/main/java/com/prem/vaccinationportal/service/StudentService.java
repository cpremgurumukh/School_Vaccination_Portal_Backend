package com.prem.vaccinationportal.service;

import com.prem.vaccinationportal.model.Student;
import com.prem.vaccinationportal.model.VaccinationDrive;
import com.prem.vaccinationportal.model.VaccinationRecord;
import com.prem.vaccinationportal.repository.StudentRepository;
import com.prem.vaccinationportal.repository.VaccinationDriveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private VaccinationDriveRepository driveRepository;

    public Student addStudent(Student student) {
        if (studentRepository.findByStudentId(student.getStudentId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ID already exists");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        if (!student.getStudentId().equals(updatedStudent.getStudentId()) &&
                studentRepository.findByStudentId(updatedStudent.getStudentId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ID already exists");
        }
        student.setName(updatedStudent.getName());
        student.setClassGrade(updatedStudent.getClassGrade());
        student.setStudentId(updatedStudent.getStudentId());
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        studentRepository.delete(student);
    }

    public List<Student> searchStudents(String name, String classGrade, String studentId, String vaccineName) {
        List<Student> students = studentRepository.findAll();
        if (vaccineName != null && !vaccineName.isEmpty()) {
            students = studentRepository.findByVaccinationRecordsVaccineName(vaccineName, Pageable.unpaged()).getContent();
        }
        return students.stream()
                .filter(s -> name == null || s.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(s -> classGrade == null || s.getClassGrade().equalsIgnoreCase(classGrade))
                .filter(s -> studentId == null || s.getStudentId().equalsIgnoreCase(studentId))
                .collect(Collectors.toList());
    }

    public Student vaccinateStudent(Long studentId, Long driveId, String vaccineName) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
        VaccinationDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Drive not found"));

        if (drive.isCompleted() || drive.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot vaccinate in past or completed drive");
        }
        if (drive.getAvailableDoses() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No doses available");
        }

        VaccinationRecord record = new VaccinationRecord();
        record.setVaccineName(vaccineName);
        record.setDate(LocalDate.now());
        record.setStudent(student);
        record.setDrive(drive);

        student.getVaccinationRecords().add(record);
        drive.setAvailableDoses(drive.getAvailableDoses() - 1);

        studentRepository.save(student);
        driveRepository.save(drive);
        return student;
    }

    public Student markVaccinated(Long studentId, Long driveId, String vaccineName) {
        return vaccinateStudent(studentId, driveId, vaccineName);
    }

    public List<Student> bulkImport(MultipartFile file) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length != 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid CSV format");
                }
                Student student = new Student();
                student.setName(data[0].trim());
                student.setClassGrade(data[1].trim());
                student.setStudentId(data[2].trim());
                students.add(student);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing file: " + e.getMessage());
        }

        List<Student> savedStudents = new ArrayList<>();
        for (Student student : students) {
            if (studentRepository.findByStudentId(student.getStudentId()) == null) {
                savedStudents.add(studentRepository.save(student));
            }
        }
        return savedStudents;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Page<Student> getVaccinatedStudents(String vaccineName, Pageable pageable) {
        if (vaccineName != null && !vaccineName.isEmpty()) {
            return studentRepository.findByVaccinationRecordsVaccineName(vaccineName, pageable);
        }
        return studentRepository.findAllWithVaccinations(pageable);
    }
}