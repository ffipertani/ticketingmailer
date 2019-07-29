package com.ff.ticketingmailer.service;

import com.ff.ticketingmailer.model.MaintenanceRemainder;
import com.ff.ticketingmailer.model.Manutenzione;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TicketingEmailService {
    private static final Logger log = LoggerFactory.getLogger(TicketingEmailService.class.toString());
    private final ExcelParseService excelParseService;
    private final PersistenceService persistenceService;
    private final EmailService emailService;
    private final Object lock = new Object();

    public TicketingEmailService(ExcelParseService excelParseService, PersistenceService persistenceService, EmailService emailService) {
        this.excelParseService = excelParseService;
        this.persistenceService = persistenceService;
        this.emailService = emailService;
    }

    public List<MaintenanceRemainder> listRemainers(){
        log.debug("Listing remainders");
        try (Connection connection = persistenceService.getDBConnection()) {
            return persistenceService.selectAll(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void manutenzioneDone(String id){
        try (Connection connection = persistenceService.getDBConnection()) {
           persistenceService.insertManutenzioneDate(connection, id);
           connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void processFile(InputStream inputStream) {
        log.info("processing file");
        synchronized (lock) {
            log.info("Started processing file");
            try (Connection connection = persistenceService.getDBConnection()) {
                List<MaintenanceRemainder> persistedRemainders = persistenceService.selectAll(connection);
                List<MaintenanceRemainder> remainders = excelParseService.parse(inputStream);
                for (MaintenanceRemainder remainder : remainders) {
                    if (!exists(remainder, persistedRemainders)) {
                        log.info("inserting a remainder {}", remainder.getId());
                        persistenceService.insertRemainder(connection, remainder);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedRate = 120000)
    public void scheduledJob() {
        log.debug("Starting scheduled job");
        List<MaintenanceRemainder> maintenanceRemainderList = null;
        synchronized (lock) {
            try (Connection connection = persistenceService.getDBConnection()) {
                maintenanceRemainderList = persistenceService.selectAll(connection);
                for (MaintenanceRemainder remainder : maintenanceRemainderList) {
                    log.info("Processing remainder {}", remainder.getId());
                    if (shouldSendARemainder(remainder)) {
                        log.info("sending email for remainder {}",remainder.getId());
                        emailService.sendEmail(remainder);
                        insertEmailDate(remainder);
                    }
                    log.info("Finished processing remainder {}", remainder.getId());
                }
            } catch (SQLException e) {
                log.error("Error processing records",e);
                e.printStackTrace();
            }
        }
    }

    private void insertEmailDate(MaintenanceRemainder maintenanceRemainder){
        try (Connection connection = persistenceService.getDBConnection()) {
            persistenceService.insertEmailDate(connection, maintenanceRemainder.getId());
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean shouldSendARemainder(MaintenanceRemainder remainder) {
        Date lastManutenzione = remainder.getDataManutenzione()!=null?remainder.getDataManutenzione():remainder.getDataInstallazione();
        Calendar gregorianCalendar = Calendar.getInstance();
        gregorianCalendar.setTime(lastManutenzione);
        gregorianCalendar.add(Calendar.MONTH, (Manutenzione.fromDescrione(remainder.getManutenzione()).getMesi()));
        return gregorianCalendar.getTime().before(new Date()) && (remainder.getDataEmail()==null || (remainder.getDataEmail()!=null && remainder.getDataEmail().before(gregorianCalendar.getTime())));
    }

    private boolean exists(MaintenanceRemainder remainder, List<MaintenanceRemainder> persistedRemainders) {
        for (MaintenanceRemainder persistedRemainder : persistedRemainders) {
            if (remainder.getId().equals(persistedRemainder.getId())) {
                return true;
            }
        }
        return false;
    }
}
