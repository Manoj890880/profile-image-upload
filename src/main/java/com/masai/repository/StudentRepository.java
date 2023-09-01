package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.model.Student;

import java.math.BigDecimal;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>{

    Optional<Object> findByRollNo(BigDecimal rollNo);
}
