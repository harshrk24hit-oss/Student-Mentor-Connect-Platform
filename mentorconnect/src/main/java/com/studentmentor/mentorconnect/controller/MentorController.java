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
@RequestMapping("/mentor")
public class MentorController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User mentor = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        List<Task> tasks = taskRepository.findByMentor(mentor); // Only tasks created by this mentor
        model.addAttribute("tasks", tasks);
        model.addAttribute("mentor", mentor);
        return "mentor-dashboard";
    }

    // Add Task - Form
    @GetMapping("/add-task")
    public String addTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "mentor-add-task";
    }

    // Add Task - Submit
    @PostMapping("/add-task")
    public String addTask(@ModelAttribute Task task, Authentication auth) {
        User mentor = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));
        task.setMentor(mentor);
        taskRepository.save(task);
        return "redirect:/mentor/dashboard";
    }

    // View Submitted Tasks
    @GetMapping("/view-submissions")
    public String viewSubmissions(Model model, Authentication auth) {
        User mentor = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        // Get all tasks created by this mentor
        List<Task> tasks = taskRepository.findByMentor(mentor);

        // Map each task to its submissions
        Map<Task, List<Submission>> taskSubmissionsMap = new HashMap<>();
        for (Task task : tasks) {
            List<Submission> submissions = submissionRepository.findByTask(task);
            taskSubmissionsMap.put(task, submissions);
        }

        model.addAttribute("taskSubmissionsMap", taskSubmissionsMap);
        model.addAttribute("mentor", mentor);
        return "mentor-view-submissions";
    }
    
 // Edit Task - Form
    @GetMapping("/edit-task/{taskId}")
    public String editTaskForm(@PathVariable Long taskId, Model model) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        model.addAttribute("task", task);
        return "mentor-edit-task";
    }

    // Edit Task - Submit
    @PostMapping("/edit-task/{taskId}")
    public String editTask(@PathVariable Long taskId, @ModelAttribute Task taskDetails) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        taskRepository.save(task);

        return "redirect:/mentor/dashboard";
    }

}
