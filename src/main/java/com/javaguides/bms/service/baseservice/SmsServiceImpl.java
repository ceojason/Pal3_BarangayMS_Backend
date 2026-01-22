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

    @Value("${textbee.api.url}") // e.g., http://localhost:8080/sms/send
    private String textBeeApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendSms(SmsModel sms) {

        // Build request body according to textbee.dev API
        Map<String, String> body = new HashMap<>();
        body.put("to", sms.getRecipient());   // recipient phone number
        body.put("message", sms.getMessage()); // message content

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(textBeeApiUrl, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("SMS sent successfully: " + response.getBody());
            } else {
                System.err.println("Failed to send SMS: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Exception while sending SMS: " + e.getMessage());
        }
    }
}