package com.masai.controller;

import com.masai.exception.ResourceNotFoundException;
import com.masai.model.Student;
import com.masai.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/webapp/imagedata";

    private static final String STUDENT_NOT_FOUND_MESSAGE = "Student not found with id: ";

    @PostMapping("/students")
    @ResponseBody
    public String saveStudent(@ModelAttribute Student stu, @RequestParam("img") MultipartFile file) {

        // Generate a unique filename using timestamp and original filename
        String originalFilename = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueFilename = timestamp + "_" + originalFilename;

        // Save the image file
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, uniqueFilename);
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving image file";
        }

        // Set the profilePic field and save the student
        stu.setProfilePic(uniqueFilename);
        studentRepository.save(stu);

        return "Save Data Successfully!";
    }

    @GetMapping("/students/{id}/profile-pic")
    public ResponseEntity<Resource> getProfilePic(@PathVariable Long id) {
        // Fetch the student from the repository by ID
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND_MESSAGE + id));

        // Load the profile picture file
        Path imagePath = Paths.get(UPLOAD_DIRECTORY, student.getProfilePic());
        Resource resource = new FileSystemResource(imagePath.toFile());

        // Determine the content type based on the file extension
        String contentType;
        try {
            contentType = Files.probeContentType(imagePath);
        } catch (IOException e) {
            contentType = "application/octet-stream"; // Fallback content type
        }

        // Create a ResponseEntity with the profile picture content and appropriate headers
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);

    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentDetails(@PathVariable Long id) {
        // Fetch the student from the repository by ID
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND_MESSAGE + id));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok()
                .headers(headers)
                .body(student);
    }

    @GetMapping("/students/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/students/by-rollNo/{rollNo}")
    @ResponseBody
    public Student getStudentByRollNo(@PathVariable BigDecimal rollNo) {
        // Fetch the student from the repository by roll number
        return (Student) studentRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND_MESSAGE + rollNo));

    }

    @PutMapping("/students/{id}")
    @ResponseBody
    public ResponseEntity<String> updateStudentDetails(
            @PathVariable Long id,
            @ModelAttribute Student updatedStudent,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic) throws IOException {
        // Fetch the student from the repository by ID
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(STUDENT_NOT_FOUND_MESSAGE + id));

        // Update the student's details
        student.setName(updatedStudent.getName());
        student.setRollNo(updatedStudent.getRollNo());
        student.setDateOfBirth(updatedStudent.getDateOfBirth());
        student.setAddress(updatedStudent.getAddress());

        // Handle profile picture update
        if (profilePic != null && !profilePic.isEmpty()) {
            String originalFilename = profilePic.getOriginalFilename();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String uniqueFilename = timestamp + "_" + originalFilename;

            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, uniqueFilename);
            Files.write(fileNameAndPath, profilePic.getBytes());
            student.setProfilePic(uniqueFilename);
        }

        // Save the updated student
        studentRepository.save(student);

        return ResponseEntity.ok("Student details updated successfully!");
    }
}
