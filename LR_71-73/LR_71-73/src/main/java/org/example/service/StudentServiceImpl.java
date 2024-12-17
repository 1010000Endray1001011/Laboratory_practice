package org.example.service;

import org.example.model.Student;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public Student getStudentById(Long id) {
        return studentMap.get(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    @Override
    public Student createStudent(Student student) {
        student.setId(currentId++);
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        studentMap.remove(id);
    }
}