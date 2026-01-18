package com.javaguides.bms.service.baseservice;

public interface EmailService {
    void sendSimpleEmailNotif(String to, String subject, String body);
}
