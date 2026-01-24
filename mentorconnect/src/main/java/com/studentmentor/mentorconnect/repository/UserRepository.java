package com.studentmentor.mentorconnect.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.studentmentor.mentorconnect.model.User;
import com.studentmentor.mentorconnect.model.Role;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);
}
