package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.AlertStatusEnum;
import com.javaguides.bms.enums.SmsTypeEnum;
import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.model.AnnouncementModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class AnnouncementReturnModel {

    private String id;
    private Integer type;
    private String typeString;
    private Integer alertStatus;
    private String alertTypeString;
    private String header;
    private String message;
    private Integer isSmsEmail;
    private String isSmsEmailString;
    private String channel;

    private String refNo;
    private String ackMessage;

    private String residentTypeString;

    private List<AnnouncementModel> announcementModels;

    public AnnouncementReturnModel(AnnouncementModel model) {
        this.id = model.getId();
        this.type = model.getType();
        this.typeString = SmsTypeEnum.getDescByKey(type);
        this.alertStatus = model.getAlertStatus();
        this.alertTypeString = AlertStatusEnum.getDesc3ByKey(alertStatus);
        this.isSmsEmail = model.getIsSmsEmail();
        this.isSmsEmailString = YesOrNoEnum.YES.getKey().equals(isSmsEmail) ? "SMS" : "Email";
        this.channel = this.isSmsEmailString;
        this.header = model.getHeader();
        this.message = model.getMessage();
        this.residentTypeString = model.getRecipientTypeString();
        this.refNo = model.getRefNo();
        this.ackMessage = model.getAckMessage();
        this.announcementModels = model.getAnnouncementModels();
    }
}
