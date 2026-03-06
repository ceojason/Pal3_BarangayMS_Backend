package com.javaguides.bms.service.baseservice;

import com.javaguides.bms.model.basemodel.SmsModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceImpl extends BaseServiceImpl implements SmsService {

    @Value("${semaphore.api.url}") // e.g., https://api.semaphore.co/api/v4/messages
    private String semaphoreApiUrl;

    @Value("${semaphore.api.key}")
    private String semaphoreApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendSms(SmsModel sms) {
        Map<String, Object> body = new HashMap<>();
        body.put("apikey", semaphoreApiKey);
        body.put("number", sms.getRecipient());
        body.put("message", sms.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(semaphoreApiUrl, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("SMS sent successfully: " + response.getBody());
            } else {
                System.err.println("Failed to send SMS: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            System.err.println("Exception while sending SMS: " + e.getMessage());
        }
    }
}