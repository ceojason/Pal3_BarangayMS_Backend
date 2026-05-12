package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.AnnouncementModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
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
    private String channelString;
    private String recipientListString;
    private String recipientFullNm;

    private String refNo;
    private String ackMessage;

    private Date createdDt;
    private String createdDtString;

    private String recipientTypeString;
    private String userId;

    private String location;
    private String date;
    private String dateString;
    private String time;

    private List<String> recipientList;
    private List<AnnouncementModel> announcementModels;

    public AnnouncementReturnModel(AnnouncementModel model) {
        this.id = model.getId();
        this.type = model.getType();
        this.typeString = LogsTypeEnum.getDescByKey(type);
        this.alertStatus = model.getAlertStatus();
        this.alertTypeString = AlertStatusEnum.getDesc3ByKey(alertStatus);
        this.isSmsEmail = model.getIsSmsEmail();
        this.isSmsEmailString = ChannelEnum.getStringByKey(model.getIsSmsEmail());
        this.channelString = this.isSmsEmailString;
        this.header = model.getHeader();
        this.message = model.getMessage();
        this.recipientTypeString = model.getRecipientTypeString();
        this.refNo = model.getRefNo();
        this.ackMessage = model.getAckMessage();
        this.announcementModels = model.getAnnouncementModels();
        this.recipientList = model.getRecipientList();
        this.recipientListString = model.getRecipientListString();
        this.userId = model.getUserId();
        this.recipientFullNm = model.getFullNm();
        this.createdDt = model.getCreatedDt();
        this.createdDtString = DateUtil.getDateStringWithFormat(createdDt, DateFormatEnum.DT_FORMAT_7.getPattern());
        this.location = model.getLocation();
        this.date = model.getDateString()!=null ? model.getDateString() : DateUtil.getDateStringWithFormat(model.getDate(), DateFormatEnum.DT_FORMAT_5.getPattern());
        this.time = model.getTime();
    }
}
