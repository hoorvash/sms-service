package org.smartech.smartech;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartech.smartech.constant.Constant;
import org.smartech.smartech.dto.GeneralDTO;
import org.smartech.smartech.enumeration.SMSStatus;
import org.smartech.smartech.exception.ServiceCallException;
import org.smartech.smartech.model.redis.SMS;
import org.smartech.smartech.repository.SMSRepository;
import org.smartech.smartech.service.HttpService;
import org.smartech.smartech.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class SMSServiceTest {

    @Value("${sms.service.numberOfRetries}")
    private int tryCount;

    @Autowired
    SMSService smsService;

    @MockBean
    SMSRepository smsRepository;

    @MockBean
    HttpService httpService;

    @Test
    public void testSuccessfulSMS() throws Exception {
        SMS sms = new SMS("1", "09355432795", "SMS Test", 1, LocalDateTime.now(), null);
        doReturn(sms).when(smsRepository).save(any());
        doReturn("").when(httpService).getRequest(any(), any());
        GeneralDTO dto = smsService.sendSMS(sms.getPhone(), sms.getBodyText());
        Assertions.assertThat(dto.getResultCode().equals(Constant.SUCCESSFUL_CODE));
        Assertions.assertThat(dto.getResultMsg().equals(Constant.SUCCESSFUL_MSG));
        Assertions.assertThat(dto.getResult().equals(SMSStatus.SENT));
    }

    @Test
    public void testFailSMS() throws Exception {
        SMS sms = new SMS("1", "09355432795", "SMS Test", 1, LocalDateTime.now(), null);
        doReturn(sms).when(smsRepository).save(any());
        doReturn(null).when(httpService).getRequest(any(), any());
        GeneralDTO dto = smsService.sendSMS(sms.getPhone(), sms.getBodyText());
        Assertions.assertThat(dto.getResultCode().equals(Constant.ExternalServiceException.SMS_FAILURE_CODE));
        Assertions.assertThat(dto.getResultMsg().equals(Constant.ExternalServiceException.SMS_FAILURE_MSG));
        Assertions.assertThat(dto.getResult().equals(SMSStatus.NOT_SUCCESSFUL));
    }

    @Test
    public void testServiceCallError() throws Exception {
        SMS sms = new SMS("1", "09355432795", "SMS Test", 1, LocalDateTime.now(), null);
        doReturn(sms).when(smsRepository).save(any());
        doReturn(null).when(httpService).getRequest(any(), any());
        GeneralDTO dto = smsService.sendSMS(sms.getPhone(), sms.getBodyText());
        Assertions.assertThat(dto.getResultCode().equals(Constant.ExternalServiceException.SERVICE_CALL_ERROR_CODE));
        Assertions.assertThat(dto.getResultMsg().equals(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG));
        Assertions.assertThat(dto.getResult().equals(SMSStatus.SMS_SERVICES_NOT_AVAILABLE));
    }

    @Test
    public void testCounter() throws ServiceCallException {
        SMS sms = new SMS("1", "09355432795", "SMS Test", 1, LocalDateTime.now(), null);
        doReturn(sms).when(smsRepository).save(any());
        List<SMS> smsList = new ArrayList<>();
        smsList.add(sms);
        doReturn(smsList).when(smsRepository).findAll();
        doThrow(new ServiceCallException(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG,
                Constant.ExternalServiceException.SERVICE_CALL_ERROR_CODE)).when(httpService).getRequest(any(), any());

        boolean isNecessary = smsService.getSMSOutOfQueueIfNecessary(sms, false);
        Assertions.assertThat(!isNecessary);

        smsService.sendAgain();
        Assertions.assertThat(sms.getTryCount() == 2);

        for (int i = 2 ; i < tryCount + 1; i++) { // more than try count
            smsService.sendAgain();
        }
        Assertions.assertThat(sms.getTryCount() == tryCount - 1); // stay equals to try count
        isNecessary = smsService.getSMSOutOfQueueIfNecessary(sms, false);
        Assertions.assertThat(isNecessary);
    }
}
