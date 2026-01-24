package com.studentmentor.mentorconnect.repository;

import java.util.List;
import java.util.Optional;
import com.studentmentor.mentorconnect.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import com.studentmentor.mentorconnect.model.Submission;
import com.studentmentor.mentorconnect.model.Task;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	
	Optional<Submission> findByTaskAndStudent(Task task, User student);
	List<Submission> findByTask(Task task);

}
