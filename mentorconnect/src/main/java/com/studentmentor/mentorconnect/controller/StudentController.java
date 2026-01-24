package com.studentmentor.mentorconnect.controller;

import com.studentmentor.mentorconnect.model.Submission;
import com.studentmentor.mentorconnect.model.Task;
import com.studentmentor.mentorconnect.model.User;
import com.studentmentor.mentorconnect.repository.SubmissionRepository;
import com.studentmentor.mentorconnect.repository.TaskRepository;
import com.studentmentor.mentorconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User student = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Task> tasks = taskRepository.findAll(); // you can filter tasks assigned to this student if needed

        // Map to track if the student submitted each task
        Map<Long, Boolean> submittedMap = new HashMap<>();
        for (Task task : tasks) {
            boolean submitted = submissionRepository.findByTaskAndStudent(task, student).isPresent();
            submittedMap.put(task.getId(), submitted);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("submittedMap", submittedMap);
        model.addAttribute("student", student);

        return "student-dashboard";
    }

    // Submit Task - Form
    @GetMapping("/submit-task/{taskId}")
    public String submitTaskForm(@PathVariable Long taskId, Model model, Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User student = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if already submitted
        submissionRepository.findByTaskAndStudent(task, student)
                .ifPresent(submission -> model.addAttribute("submittedAnswer", submission.getAnswer()));

        model.addAttribute("task", task);
        model.addAttribute("student", student);
        return "student-submit-task";
    }

    // Submit Task - Submit
    @PostMapping("/submit-task/{taskId}")
    public String submitTask(@PathVariable Long taskId, @RequestParam String answer, Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User student = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Only save if not already submitted
        if (submissionRepository.findByTaskAndStudent(task, student).isEmpty()) {
            Submission submission = new Submission();
            submission.setTask(task);
            submission.setStudent(student);
            submission.setAnswer(answer);
            submissionRepository.save(submission);
        }

        return "redirect:/student/dashboard";
    }

    // View Submission
    @GetMapping("/view-task/{taskId}")
    public String viewTask(@PathVariable Long taskId, Model model, Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User student = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Submission submission = submissionRepository.findByTaskAndStudent(task, student)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        model.addAttribute("task", task);
        model.addAttribute("submission", submission);
        return "student-view-task";
    }
}
