package com.lcwd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/")
    public String showForm() {
        return "index";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("message") String message,
                            @RequestParam("subject") String subject,
                            @RequestParam("to") String to,
                            @RequestParam("from") String from,
                            @RequestParam("file") MultipartFile file,
                            Model model) {
        String[] recipients = to.split(",");
        try {
            emailService.sendEmail(message, subject, recipients, from, file);
            model.addAttribute("message", "Email sent successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error sending email: " + e.getMessage());
        }
        return "index";
    }
}
