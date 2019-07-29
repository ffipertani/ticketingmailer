package com.ff.ticketingmailer;

import com.ff.ticketingmailer.service.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.Connection;
import java.sql.SQLException;

@ServletComponentScan
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.ff")
public class Application {
    @Autowired
    private PersistenceService persistenceService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent ctxStartEvt) {
        System.out.println("Context Start Event received.");
        Connection connection = persistenceService.getDBConnection();
        try {
            persistenceService.createRemainder(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
