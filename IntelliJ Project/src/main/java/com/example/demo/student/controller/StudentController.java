package com.example.demo.student.controller;

import com.example.demo.student.model.Student;
import com.example.demo.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling student-related requests.
 * Provides endpoints to retrieve all students and a specific student by ID.
 */
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    // Service to handle student operations
    private final StudentService studentService;

    /**
     * Retrieves a list of all students.
     * @return List of students.
     */
    @GetMapping(path = "")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    /**
     * Retrieves a specific student by ID.
     * @param studentId ID of the student to retrieve.
     * @return Student details.
     */
    @GetMapping(path = "/{studentId}")
    public Student getStudent(@PathVariable Long studentId) {
        return studentService.getStudent(studentId);
    }

    @PostMapping
    public void addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
    }
}
