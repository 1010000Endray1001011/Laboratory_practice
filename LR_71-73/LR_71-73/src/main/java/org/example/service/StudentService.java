package org.example.service;


import org.example.model.Student;
import java.util.List;

public interface StudentService {
    Student getStudentById(Long id);
    List<Student> getAllStudents();
    Student createStudent(Student student);
    void deleteStudent(Long id);
}