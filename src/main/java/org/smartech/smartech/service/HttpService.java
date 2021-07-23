package org.smartech.smartech.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartech.smartech.constant.Constant;
import org.smartech.smartech.exception.ServiceCallException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpService {

    private static Logger log = LoggerFactory.getLogger(HttpService.class);

    public String getRequest(String uri, String token) throws ServiceCallException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header(Constant.HttpService.CONTENT_TYPE, Constant.HttpService.APPLICATION_JSON)
                .header(Constant.HttpService.ACCEPT, Constant.HttpService.APPLICATION_JSON)
                .header(Constant.HttpService.HEADER_STRING, Constant.HttpService.TOKEN_PREFIX + token)
                .GET() // GET is default
                .build();
        return call(request);
    }

    private String call(HttpRequest request) throws ServiceCallException {
        HttpResponse<String> response;
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()/100 != 2) {
                log.error("Error {} with response statusCode {} ", Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG,
                        response.statusCode());
                throw new ServiceCallException(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG,
                        response.statusCode());
            }
        } catch (Exception e) {
            log.error("Error {} with Code {} ", Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG,
                    Constant.ExternalServiceException.SERVICE_CALL_ERROR_CODE, e);
            throw new ServiceCallException(Constant.ExternalServiceException.SERVICE_CALL_ERROR_MSG, e);
        }
        return response.body();
    }
}
