package org.smartech.smartech;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartech.smartech.model.redis.SMS;
import org.smartech.smartech.repository.SMSRepository;
import org.smartech.smartech.repository.elasticsearch.SMSLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@SuppressWarnings("all")
public class SMSRepositoryTest {

    @Autowired
    SMSRepository smsRepository;
    @Autowired
    SMSLogRepository smsLogRepository;

    @BeforeEach
    public void deleteFirst() {
        smsRepository.findById("Test_1").ifPresent(sms -> smsRepository.delete(sms));
        smsLogRepository.findById("Test_1").ifPresent(smsLog -> smsLogRepository.delete(smsLog));
    }

    @AfterEach
    public void delete() {
        smsRepository.findById("Test_1").ifPresent(sms -> smsRepository.delete(sms));
        smsLogRepository.findById("Test_1").ifPresent(smsLog -> smsLogRepository.delete(smsLog));
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
