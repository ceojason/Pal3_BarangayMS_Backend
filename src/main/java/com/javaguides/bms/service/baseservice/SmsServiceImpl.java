package com.javaguides.bms.service.baseservice;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl extends BaseServiceImpl implements SmsService {

//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Value("${twilio.phone.number}")
//    private String twilioPhoneNumber;

    public void sendSms(String phoneNumber) {
//        Twilio.init(accountSid, authToken);
//        Message.creator(
//                new com.twilio.type.PhoneNumber(phoneNumber),
//                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
//                "User account was registered successfully!"
//        ).create();
    }
}