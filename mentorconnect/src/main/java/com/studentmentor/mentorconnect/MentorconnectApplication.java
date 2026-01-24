package com.studentmentor.mentorconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.studentmentor.mentorconnect.model.Role;
import com.studentmentor.mentorconnect.model.User;
import com.studentmentor.mentorconnect.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MentorconnectApplication {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(MentorconnectApplication.class, args);
    }

    // This runs after the application starts
    @PostConstruct
    public void init() {
        User admin = userRepository.findByEmail("admin@example.com")
                .orElse(new User());

        admin.setName("Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);
    }

}

