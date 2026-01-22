package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.AlertStatusEnum;
import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.SmsTypeEnum;
import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.NotifLogsModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class NotifLogsReturnModel {

    private String id;
    private String userId;
    private Integer status;
    private String statusString;

    private String message;
    private String recipient;
    private String refNo;
    private Integer isSmsEmail;
    private String isSmsEmailString;

    private Integer type;
    private String typeString;

    private Date sentDt;
    private String sentDtString;

    public NotifLogsReturnModel(NotifLogsModel model) {
        this.id = model.getId();
        this.refNo = model.getRefNo();
        this.message = model.getMessage();
        this.recipient = model.getRecipient();
        this.isSmsEmail = model.getIsSmsEmail();
        this.isSmsEmailString = isSmsEmail!=null && isSmsEmail.equals(YesOrNoEnum.YES.getKey()) ? "SMS" : "Email";
        this.sentDt = model.getSentDt();
        this.sentDtString = DateUtil.getDateStringWithFormat(sentDt, DateFormatEnum.DT_FORMAT_7.getPattern());
        this.userId = model.getUserId();

        this.type = model.getType();
        this.typeString = SmsTypeEnum.getDescByKey(type);

        this.status = model.getStatus();
        this.statusString = AlertStatusEnum.getDesc3ByKey(status);
    }
}
