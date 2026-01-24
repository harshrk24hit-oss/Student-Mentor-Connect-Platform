package com.studentmentor.mentorconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.studentmentor.mentorconnect.model.Answer;
import com.studentmentor.mentorconnect.model.Task;
import com.studentmentor.mentorconnect.model.User;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
