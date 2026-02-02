package com.studentmentor.mentorconnect.controller;

import com.studentmentor.mentorconnect.model.Role;
import com.studentmentor.mentorconnect.model.User;
import com.studentmentor.mentorconnect.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    // ===================== ADMIN DASHBOARD =====================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> students = userRepository.findByRole(Role.STUDENT);
        List<User> mentors = userRepository.findByRole(Role.MENTOR);

        model.addAttribute("students", students);
        model.addAttribute("mentors", mentors);

        return "admin-dashboard";
    }

    // ===================== ADD STUDENT =====================
    @GetMapping("/add-student")
    public String addStudentForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-add-student";
    }

    @PostMapping("/add-student")
    public String addStudent(@ModelAttribute User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.STUDENT);

        userRepository.save(user);
        return "redirect:/admin/dashboard";
    }


    // ===================== ADD MENTOR =====================
    @GetMapping("/add-mentor")
    public String addMentorForm(Model model) {
        model.addAttribute("user", new User());
        return "admin-add-mentor";
    }

    @PostMapping("/add-mentor")
    public String addMentor(@ModelAttribute User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword())); 
        user.setRole(Role.MENTOR);

        userRepository.save(user);
        return "redirect:/admin/dashboard";
    }


    // ===================== ASSIGN MENTOR =====================
    @GetMapping("/assign-mentor/{studentId}")
    public String assignMentorForm(@PathVariable Long studentId, Model model) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<User> mentors = userRepository.findByRole(Role.MENTOR);

        model.addAttribute("student", student);
        model.addAttribute("mentors", mentors);

        return "admin-assign-mentor";
    }

    @PostMapping("/assign-mentor/{studentId}")
    public String assignMentor(
            @PathVariable Long studentId,
            @RequestParam Long mentorId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        student.setMentor(mentor);
        userRepository.save(student);

        return "redirect:/admin/dashboard";
    }

    // ===================== VIEW ALL USERS =====================
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin-view-users";
    }
 // ===================== EDIT USER =====================
    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values()); // for dropdown
        return "admin-edit-user";
    }

    // ===================== UPDATE USER =====================
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute User user) {

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setRole(user.getRole());

        userRepository.save(existingUser);

        return "redirect:/admin/users";
    }

}
