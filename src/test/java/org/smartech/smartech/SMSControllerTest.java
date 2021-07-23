package org.smartech.smartech;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.smartech.smartech.constant.Constant;
import org.smartech.smartech.controller.SMSController;
import org.smartech.smartech.dto.GeneralDTO;
import org.smartech.smartech.enumeration.SMSStatus;
import org.smartech.smartech.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.smartech.smartech.enumeration.SMSStatus.SENT;

@WebMvcTest(SMSController.class)
public class SMSControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    SMSService smsService;

    @Test
    public void testSuccessfulSMS() throws Exception {

        GeneralDTO dto = new GeneralDTO();
        dto.setResult(SENT);
        dto.setResultMsg(Constant.SUCCESSFUL_MSG);
        dto.setResultCode(Constant.SUCCESSFUL_CODE);

        Mockito.when(smsService.sendSMS(any(), any())).thenReturn(dto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8081/sms/send/09355432795/Hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONAssert.assertEquals(
                "{\"resultMsg\":\"Successful\",\"resultCode\":0,\"result\":\"SENT\"}",
                result.getResponse().getContentAsString(), false);

        JSONAssert.assertEquals(HttpStatus.OK.value() + "", result.getResponse()
                .getStatus() + "", false);
    }

    @Test
    public void testFailSMS() throws Exception {

        GeneralDTO dto = new GeneralDTO();
        dto.setResult(SMSStatus.NOT_SUCCESSFUL);
        dto.setResultMsg(Constant.ExternalServiceException.SMS_FAILURE_MSG);
        dto.setResultCode(Constant.ExternalServiceException.SMS_FAILURE_CODE);

        Mockito.when(smsService.sendSMS(any(), any())).thenReturn(dto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8081/sms/send/09355432795/Hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONAssert.assertEquals(
                "{\"resultMsg\":\"Sms failure\",\"resultCode\":1003,\"result\":\"NOT_SUCCESSFUL\"}",
                result.getResponse().getContentAsString(), false);

        JSONAssert.assertEquals(HttpStatus.OK.value() + "", result.getResponse()
                .getStatus() + "", false);
    }

    @Test
    public void testServiceCallError() throws Exception {

        GeneralDTO dto = new GeneralDTO();
        dto.setResult(SMSStatus.SMS_SERVICES_NOT_AVAILABLE);
        dto.setResultMsg(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG);
        dto.setResultCode(Constant.ExternalServiceException.SERVICE_CALL_ERROR_CODE);

        Mockito.when(smsService.sendSMS(any(), any())).thenReturn(dto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("http://localhost:8081/sms/send/09355432795/Hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONAssert.assertEquals(
                "{\"resultMsg\":\"Service Call Error\",\"resultCode\":1000,\"result\":\"SMS_SERVICES_NOT_AVAILABLE\"}",
                result.getResponse().getContentAsString(), false);

        JSONAssert.assertEquals(HttpStatus.OK.value() + "", result.getResponse()
                .getStatus() + "", false);
    }
}
