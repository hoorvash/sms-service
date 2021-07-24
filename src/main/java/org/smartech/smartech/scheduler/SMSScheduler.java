package org.smartech.smartech.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartech.smartech.service.SMSService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SMSScheduler {

    private static Logger log = LoggerFactory.getLogger(SMSScheduler.class);
    private final SMSService smsService;

    public SMSScheduler(SMSService smsService) {
        this.smsService = smsService;
    }

    @Scheduled(cron = "0 0 0/2 ? * *")
//    @Scheduled(fixedDelay = 30000, initialDelay = 5000)
    public void sendSMS() {
        log.info("Scheduler started :: ");
        smsService.sendAgain();
        log.info("Scheduler ended :: ");
    }
}
