package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.AlertStatusEnum;
import com.javaguides.bms.enums.ChannelEnum;
import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.LogsTypeEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_announcement")
@TableAlias("ta")
public class AnnouncementModel extends BaseModel {

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "GRP_ID")
    private String grpId;

    @Column(name = "HEADER")
    private String header;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "ALERT_STATUS")
    private Integer alertStatus;

    @Column(name = "CHANNEL")
    private Integer isSmsEmail;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATED_DT")
    private Date createdDt;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "ANN_DATE")
    private Date date;

    @Transient
    private String dateString;

    @Column(name = "TIME")
    private String time;

    @Transient
    private List<Integer> recipientKeys;

    @Transient
    private List<String> recipientList;

    @Transient
    private String recipientListString;

    @Transient
    private String recipientNm;

    @Transient
    private String mobileNo;

    public String formattedMobileNo() {
        if (mobileNo!=null && !mobileNo.isEmpty()) {
            return "+63" + mobileNo.substring(1);
        }
        return "";
    }

    @Transient
    private String emailAddress;

    @Transient
    private String typeString;

    @Transient
    private String alertTypeString;

    @Transient
    private String recipientTypeString;

    @Transient
    private String channelString;

    public String getAlertStatusString() {
        return AlertStatusEnum.getDesc3ByKey(alertStatus);
    }

    public String getTypeString() {
        return LogsTypeEnum.getDescByKey(type);
    }

    public String getMessageTypeString() {
        return getTypeString() + " - " + getAlertStatusString();
    }

    public String getCreatedDtString() {
        return DateUtil.getDateStringWithFormat(createdDt, DateFormatEnum.DT_FORMAT_5.getPattern() + " " + DateFormatEnum.DT_FORMAT_14.getPattern());
    }

    public String getIsSmsEmailString() {
        return ChannelEnum.getStringByKey(isSmsEmail);
    }

    @Transient
    private String firstNm;

    @Transient
    private String middleNm;

    @Transient
    private String lastNm;

    @Transient
    private String suffix;

    public String getFullNm2() {
        StringBuilder fullNm = new StringBuilder()
                .append(firstNm!=null ? firstNm : "")
                .append(" ")
                .append(middleNm!=null ? middleNm + " " : "")
                .append(lastNm)
                .append(suffix!=null ? " " + suffix : "");
        return fullNm.toString();
    }

    public String getFullNm() {
        StringBuilder fullNm = new StringBuilder();
        if (lastNm!=null) {
            fullNm.append(lastNm);
        }
        if (firstNm!=null) {
            fullNm.append(", ")
                    .append(firstNm);
        }
        if (middleNm!=null) {
            fullNm.append(", ")
                    .append(middleNm);
        }
        if (suffix!=null) {
            fullNm.append(" ")
                    .append(suffix);
        }
        return fullNm.toString();
    }

    @Transient
    private List<AnnouncementModel> announcementModels;

    public AnnouncementModel(EnrollmentRequest request) {
        if (request!=null) {
            setId(request.getId());
            setHeader(request.getHeader());
            setType(request.getType());
            setAlertStatus(request.getAlertStatus());
            setIsSmsEmail(request.getIsSmsEmail());
            setMessage(request.getMessage());
            setRecipientKeys(request.getRecipientKeys());
            setDate(request.getDate());
            setTime(request.getTime());
            setLocation(request.getLocation());
        }
    }

}
