package org.smartech.smartech;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.smartech.smartech.model.redis.SMS;
import org.smartech.smartech.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@SuppressWarnings("all")
public class SMSRepositoryTest {

    @Autowired
    SMSRepository smsRepository;

    @AfterEach
    public void delete() {
        smsRepository.findById("Test_1").ifPresent(sms -> smsRepository.delete(sms));
    }

    @Test
    public void testCreateQueue() {
        createSMS();
        Iterable<SMS> smsList = smsRepository.findAll();
        Assertions.assertThat(smsList).extracting(SMS::getPhone).contains("09350000000");
    }

    @Test
    public void testFindById() {
        SMS sms = createSMS();
        sms = smsRepository.findById("Test_1").orElseThrow();
        Assertions.assertThat(sms).isNotEqualTo(null);
    }

    @Test
    public void testDelete() {
        SMS sms = createSMS();
        smsRepository.deleteById("Test_1");
        sms = smsRepository.findById("Test_1").orElse(null);
        Assertions.assertThat(sms).isEqualTo(null);
    }

    private SMS createSMS() {
        SMS sms = SMS.builder()
                .id("Test_1")
                .phone("09350000000")
                .bodyText("Hello")
                .dateTime(LocalDateTime.now())
                .tryCount(1)
                .build();
        return smsRepository.save(sms);
    }
}
