package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.CommReportTypeEnum;
import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.PriorityEnum;
import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.CommReportModel;
import com.javaguides.bms.model.UsersModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class CommReportReturn {

    private String id;
    private String userId;
    private String refNo;
    private Integer reportTypeKey;
    private String reportTypeKeyString;
    private String description;
    private String location;
    private Integer priority;
    private String priorityString;
    private String assigneeId;
    private String remarks;

    private Integer happensNearOrInHousehold;
    private String happensNearOrInHouseholdString;
    private String othTitle;

    private String assigneeNmAndPosition;
    private List<String> attachmentUrls;
    private String ackMessage;

    private String reporter;
    private Date createdDt;
    private Date updatedDt;
    private String createdDtString;
    private String updatedDtString;
    private String mobileNo;
    private String emailAddress;
    private Integer status;


    public CommReportReturn(CommReportModel modelObj) {
        setId(modelObj.getId());
        setUserId(modelObj.getUserId());
        setRefNo(modelObj.getRefNo());
        setReportTypeKey(modelObj.getReportTypeKey());
        setReportTypeKeyString(CommReportTypeEnum.getDescBKey(reportTypeKey));
        setDescription(modelObj.getDescription());
        setLocation(modelObj.getLocation());
        setPriority(modelObj.getPriority());
        setPriorityString(PriorityEnum.getDescByKey(priority));
        setAssigneeId(modelObj.getAssigneeId());
        setHappensNearOrInHousehold(modelObj.getHappensNearOrInHousehold());
        setHappensNearOrInHouseholdString(YesOrNoEnum.getDescByKey(modelObj.getHappensNearOrInHousehold()));
        setAttachmentUrls(modelObj.getAttachmentUrls());
        setOthTitle(modelObj.getOthTitle());

        setReporter(modelObj.getFullNm());
        setCreatedDt(modelObj.getCreatedDt());
        setCreatedDtString(DateUtil.getDateStringWithFormat(modelObj.getCreatedDt(), DateFormatEnum.DT_FORMAT_1.getPattern()));
        setUpdatedDtString(DateUtil.getDateStringWithFormat(modelObj.getUpdatedDt(), DateFormatEnum.DT_FORMAT_1.getPattern()));
        setUpdatedDt(modelObj.getUpdatedDt());
        setMobileNo(modelObj.getMobileNo());
        setEmailAddress(modelObj.getEmailAddress());
        setStatus(modelObj.getStatus());
        setRemarks(modelObj.getRemarks());
    }
}
