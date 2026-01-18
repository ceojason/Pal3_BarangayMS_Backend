package com.javaguides.bms.model.basemodel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SmsModel {

    private String recipient;
    private String message;

}
