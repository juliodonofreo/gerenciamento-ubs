package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}