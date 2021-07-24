package org.smartech.smartech.constant;

public class Constant {

    public static final String SUCCESSFUL_MSG = "Successful";
    public static final Integer SUCCESSFUL_CODE = 0;
    public static final String GENERAL_ERROR_MSG = "General Error";
    public static final Integer GENERAL_ERROR_CODE = 1;

    public static class HttpService {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String APPLICATION_JSON = "application/json";
        public static final String ACCEPT = "Accept";
    }

    public static class ExternalServiceException {
        public static final String SERVICE_CALL_ERROR_MSG = "Service Call Error";
        public static final Integer SERVICE_CALL_ERROR_CODE = 1000;
        public static final String SMS1_SERVICE_ERROR_MSG = "SMS1 Service Error";
        public static final Integer SMS1_SERVICE_ERROR_CODE = 1001;
        public static final String SMS2_SERVICE_ERROR_MSG = "SMS2 Service Error";
        public static final Integer SMS2_SERVICE_ERROR_CODE = 1002;
        public static final String SMS_FAILURE_MSG = "Sms failure";
        public static final Integer SMS_FAILURE_CODE = 1003;
    }

    public static class Validation {
        public static final String INVALID_MOBILE_NUMBER_MSG = "Invalid Mobile Number";
        public static final Integer INVALID_MOBILE_NUMBER_CODE = 1004;
    }

}
