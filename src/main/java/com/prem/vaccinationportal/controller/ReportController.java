package com.prem.vaccinationportal.controller;

import com.opencsv.CSVWriter;
import com.prem.vaccinationportal.model.Student;
import com.prem.vaccinationportal.model.VaccinationRecord;
import com.prem.vaccinationportal.service.StudentService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<Student>> getVaccinationReport(
            @RequestParam(required = false) String vaccineName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(studentService.getVaccinatedStudents(vaccineName, pageable));
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam(required = false) String vaccineName,
            @RequestParam String format) throws IOException {
        List<Student> students = studentService.getVaccinatedStudents(vaccineName, Pageable.unpaged()).getContent();
        byte[] content;
        String filename;
        MediaType mediaType;

        switch (format.toLowerCase()) {
            case "csv":
                content = generateCsv(students);
                filename = "vaccination_report.csv";
                mediaType = MediaType.parseMediaType("text/csv");
                break;
            case "excel":
                content = generateExcel(students);
                filename = "vaccination_report.xlsx";
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
            case "pdf":
                content = generateLatex(students);
                filename = "vaccination_report.pdf";
                mediaType = MediaType.APPLICATION_PDF;
                break;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(content);
    }

    private byte[] generateCsv(List<Student> students) throws IOException {
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(new String[]{"Name", "Class", "Student ID", "Vaccine Name", "Date", "Status"});
            for (Student student : students) {
                for (VaccinationRecord record : student.getVaccinationRecords()) {
                    csvWriter.writeNext(new String[]{
                            student.getName(),
                            student.getClassGrade(),
                            student.getStudentId(),
                            record.getVaccineName(),
                            record.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                            "Vaccinated"
                    });
                }
            }
        }
        return stringWriter.toString().getBytes();
    }

    private byte[] generateExcel(List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Vaccination Report");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Name", "Class", "Student ID", "Vaccine Name", "Date", "Status"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            int rowNum = 1;
            for (Student student : students) {
                for (VaccinationRecord record : student.getVaccinationRecords()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(student.getName());
                    row.createCell(1).setCellValue(student.getClassGrade());
                    row.createCell(2).setCellValue(student.getStudentId());
                    row.createCell(3).setCellValue(record.getVaccineName());
                    row.createCell(4).setCellValue(record.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    row.createCell(5).setCellValue("Vaccinated");
                }
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel: " + e.getMessage());
        }
    }

    private byte[] generateLatex(List<Student> students) {
        StringBuilder latex = new StringBuilder();
        latex.append("\\documentclass{article}\n");
        latex.append("\\usepackage{booktabs}\n");
        latex.append("\\usepackage{geometry}\n");
        latex.append("\\geometry{a4paper, margin=1in}\n");
        latex.append("\\begin{document}\n");
        latex.append("\\title{Vaccination Report}\n");
        latex.append("\\author{}\n");
        latex.append("\\date{}\n");
        latex.append("\\maketitle\n");
        latex.append("\\begin{table}[h]\n");
        latex.append("\\centering\n");
        latex.append("\\begin{tabular}{llllll}\n");
        latex.append("\\toprule\n");
        latex.append("Name & Class & Student ID & Vaccine Name & Date & Status \\\\ \n");
        latex.append("\\midrule\n");
        for (Student student : students) {
            for (VaccinationRecord record : student.getVaccinationRecords()) {
                latex.append(String.format(
                        "%s & %s & %s & %s & %s & Vaccinated \\\\ \n",
                        escapeLatex(student.getName()),
                        escapeLatex(student.getClassGrade()),
                        escapeLatex(student.getStudentId()),
                        escapeLatex(record.getVaccineName()),
                        record.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                ));
            }
        }
        latex.append("\\bottomrule\n");
        latex.append("\\end{tabular}\n");
        latex.append("\\caption{Vaccination Report}\n");
        latex.append("\\end{table}\n");
        latex.append("\\end{document}\n");
        return latex.toString().getBytes();
    }

    private String escapeLatex(String text) {
        return text.replace("&", "\\&")
                .replace("%", "\\%")
                .replace("$", "\\$")
                .replace("#", "\\#")
                .replace("_", "\\_")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("~", "\\textasciitilde")
                .replace("^", "\\textasciicircum")
                .replace("\\", "\\textbackslash");
    }
}