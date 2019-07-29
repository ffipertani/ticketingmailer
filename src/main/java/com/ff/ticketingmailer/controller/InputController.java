package com.ff.ticketingmailer.controller;

import com.ff.ticketingmailer.service.TicketingEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class InputController {
    private final TicketingEmailService ticketingEmailService;

    public InputController(TicketingEmailService ticketingEmailService) {
        this.ticketingEmailService = ticketingEmailService;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("maintenanceRemainders", ticketingEmailService.listRemainers());
        return "flags";
    }

    @PostMapping("/upload.html")
    public String handleFileUpload(@RequestParam("fileToUpload") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        ticketingEmailService.processFile(file.getInputStream());
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}