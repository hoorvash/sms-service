package org.smartech.smartech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartech.smartech.constant.Constant;
import org.smartech.smartech.dto.GeneralDTO;
import org.smartech.smartech.enumeration.SMSStatus;
import org.smartech.smartech.exception.ServiceCallException;
import org.smartech.smartech.model.redis.SMS;
import org.smartech.smartech.repository.SMSRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SMSService {
    private static Logger log = LoggerFactory.getLogger(SMSService.class);

    @Value("${sms.service.url1}")
    private String url1;

    @Value("${sms.service.url2}")
    private String url2;

    @Value("${sms.service.numberOfRetries}")
    private Integer numberOfRetries;

    private final HttpService httpService;
    private final SMSRepository smsRepository;

    public SMSService(HttpService httpService, SMSRepository smsRepository) {
        this.httpService = httpService;
        this.smsRepository = smsRepository;
    }

    public GeneralDTO sendSMS(String number, String bodyText) {
        String response;
        GeneralDTO dto = new GeneralDTO();
        try {
            response = callSMSService(number, bodyText);
        } catch (ServiceCallException e) {
            saveNotSentSMS(number, bodyText);
            dto.setResultCode(Constant.ExternalServiceException.SERVICE_CALL_ERROR_CODE);
            dto.setResultMsg(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG);
            dto.setResult(SMSStatus.SMS_SERVICES_NOT_AVAILABLE);
            return dto;
        }
        log.debug("Sms sent for number {} with message {} get response {} ", number, bodyText, response);
        return interpretResponse(response);
    }

    private GeneralDTO interpretResponse(String response) {
        // check if service response code shows "successfully sent"
        GeneralDTO dto = new GeneralDTO();
        if (response == null) { // or any other parsing and deciding
            dto.setResultCode(Constant.ExternalServiceException.SMS_FAILURE_CODE);
            dto.setResultMsg(Constant.ExternalServiceException.SMS_FAILURE_MSG);
            dto.setResult(SMSStatus.NOT_SUCCESSFUL);
            return dto;
        }
        dto.setResultCode(Constant.SUCCESSFUL_CODE);
        dto.setResultMsg(Constant.SUCCESSFUL_MSG);
        dto.setResult(SMSStatus.SENT);
        return dto;
    }

    private String callSMSService(String number, String bodyText) throws ServiceCallException {
        try {
            return httpService.getRequest(url1 + "?number=" + number + "&body=" + bodyText, "");
        } catch (ServiceCallException e) {
            log.error(Constant.ExternalServiceException.SMS1_SERVICE_ERROR_MSG, e);
            try {
                return httpService.getRequest(url2 + "?number=" + number + "&body=" + bodyText, "");
            } catch (ServiceCallException ex) {
                log.error("Error with msg {} and code {}", Constant.ExternalServiceException.SMS2_SERVICE_ERROR_MSG,
                        Constant.ExternalServiceException.SMS2_SERVICE_ERROR_CODE, e);
                throw ex;
            }
        }
    }

    private void saveNotSentSMS(String number, String bodyText) {
        SMS sms = SMS.builder()
                .phone(number)
                .bodyText(bodyText)
                .tryCount(1)
                .dateTime(LocalDateTime.now())
                .build();
        smsRepository.save(sms);
    }

    public void sendAgain() {
        smsRepository.findAll().forEach(sms -> {
            try {
                callSMSService(sms.getPhone(), sms.getBodyText());
                getSMSOutOfQueueIfNecessary(sms, true);
            } catch (ServiceCallException e) {
                boolean isDeleted = getSMSOutOfQueueIfNecessary(sms, false);
                if (!isDeleted) {
                    saveNonSentSMSAfterRetry(sms);
                }
            }
        });
    }

    @Transactional
    public boolean getSMSOutOfQueueIfNecessary(SMS sms, boolean instantDelete) {
        boolean isEligible = instantDelete || sms.getTryCount() >= numberOfRetries - 1;
        if (isEligible) {
            log.info(" Deleting sms with number {} and text {} with number of tries {} because {} " ,
                            sms.getPhone(), sms.getBodyText(), sms.getTryCount(),
                    (instantDelete) ? "It's been sent" : "Try number reached to the limit");
            smsRepository.delete(sms);
        }
        return isEligible;
    }

    @Transactional
    public void saveNonSentSMSAfterRetry(SMS sms) {
        sms.setLastTryDateTime(LocalDateTime.now());
        sms.setTryCount(sms.getTryCount() + 1);
        smsRepository.save(sms);
    }
}
