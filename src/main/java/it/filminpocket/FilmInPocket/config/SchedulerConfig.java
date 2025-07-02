package it.filminpocket.FilmInPocket.config;

import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @Scheduled(fixedRate = 43200000)
    public void rechargeTicketsScheduledTask(){
        System.out.println("Esecuzione per la ricarica dei film tickets: "+ LocalDateTime.now());
        userCardAcquisitionService.performScheduledTicketRechargeForAllUsers();
    }
}
