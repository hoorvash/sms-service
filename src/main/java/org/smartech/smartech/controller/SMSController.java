package org.smartech.smartech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.smartech.smartech.dto.GeneralDTO;
import org.smartech.smartech.service.SMSService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sms")
public class SMSController {

    private final SMSService smsService;

    public SMSController(SMSService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/send/{number}/{body}")
    public ResponseEntity<?> sendSMS(@PathVariable("number") String number, @PathVariable("body") String body) throws JsonProcessingException {
        try {
            GeneralDTO result = smsService.sendSMS(number, body);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
    }
}
