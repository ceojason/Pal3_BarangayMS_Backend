package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_announcement")
@TableAlias("ta")
public class AnnouncementModel extends BaseModel {

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

    @Transient
    private List<Integer> recipientKeys;

    @Transient
    private String recipientListString;

    @Transient
    private String recipientNm;

    @Transient
    private String typeString;

    @Transient
    private String alertTypeString;

    @Transient
    private String recipientTypeString;

    @Transient
    private String channelString;

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
        }
    }

}
