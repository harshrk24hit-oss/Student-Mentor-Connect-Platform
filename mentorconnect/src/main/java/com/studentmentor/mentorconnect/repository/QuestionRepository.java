package com.studentmentor.mentorconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.studentmentor.mentorconnect.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {}
