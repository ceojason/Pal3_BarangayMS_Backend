package com.javaguides.sps.service.baseservice;

public interface EmailService {
    void sendSimpleEmailNotif(String to, String subject, String body);
}
