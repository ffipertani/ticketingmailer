package com.ff.ticketingmailer.service;

import com.ff.ticketingmailer.model.MaintenanceRemainder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(MaintenanceRemainder maintenanceRemainder){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("remainder@ticketing-email-service.com");
        message.setTo("francesco.fipertani@gmail.com");
        message.setSubject("Avviso manutenzione");
        message.setText("Manutenzione programmata per "+maintenanceRemainder.getId()+"  "+maintenanceRemainder.getMarca()+" "+maintenanceRemainder.getModello());
        emailSender.send(message);
    }
}
