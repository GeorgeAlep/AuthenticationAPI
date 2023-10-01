package com.example.demo.student.service;

import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Service responsible for student-related operations.
 * Handles the logic for retrieving all students and a specific student by ID.
 */
@Service
@RequiredArgsConstructor
public class StudentService {

    // Repository to interact with student data in the database
    private final StudentRepository studentRepository;

    /**
     * Retrieves a list of all students.
     * @return List of students.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retrieves a specific student by ID.
     * @param studentId ID of the student to retrieve.
     * @return Student details.
     */
    public Student getStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new IllegalStateException("Student does not exist.");
        }

        return studentOptional.get();
    }

    public void addStudent(Student student) {
        studentRepository.save(student);
    }
}
