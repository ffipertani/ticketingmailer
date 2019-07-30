package com.ff.ticketingmailer.service;

import com.ff.ticketingmailer.model.MaintenanceRemainder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
    private final JavaMailSender emailSender;
    private final String from;
    private final String to;

    public EmailService(JavaMailSender emailSender,
                        @Value( "${mail.from}" )String from,
                        @Value( "${mail.to}" )String to) {
        this.emailSender = emailSender;
        this.from = from;
        this.to = to;
    }

    public void sendEmail(MaintenanceRemainder maintenanceRemainder){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Avviso manutenzione");
        message.setText("Manutenzione programmata per "+maintenanceRemainder.getId()+"  "+maintenanceRemainder.getMarca()+" "+maintenanceRemainder.getModello());
        emailSender.send(message);
    }
}
