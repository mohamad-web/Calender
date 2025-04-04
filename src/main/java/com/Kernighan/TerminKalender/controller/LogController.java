package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.SystemLog;
import com.Kernighan.TerminKalender.repository.SystemLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/logs")
public class LogController {
    private final SystemLogRepository systemLogRepository;

    public LogController(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @GetMapping
    @Transactional
    public String getLogs(Model model) {
        List<SystemLog> logs = systemLogRepository.findAllByOrderByTimestampDesc();
        System.out.println("Logs geladen: " + logs); // Debugging
        model.addAttribute("logs", logs);
        return "admin/logs";
    }
}
