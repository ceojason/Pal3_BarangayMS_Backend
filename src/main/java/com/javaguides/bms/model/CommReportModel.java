package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_comm_report")
@TableAlias("tcr")
public class CommReportModel extends BaseModel {

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "REPORT_TYPE_KEY")
    private Integer reportTypeKey;

    @Column(name = "OTH_TITLE")
    private String othTitle;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "ASSIGNEE_ID")
    private String assigneeId;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "CREATED_DT")
    private Date createdDt;

    @Column(name = "UPDATED_DT")
    private Date updatedDt;

    @Transient
    private String firstNm;

    @Transient
    private String middleNm;

    @Transient
    private String lastNm;

    @Transient
    private String suffix;

    @Transient
    private String mobileNo;

    @Transient
    private String emailAddress;

    @Transient
    private Integer happensNearOrInHousehold;

    @Transient
    private List<MultipartFile> attachments;

    @Transient
    private List<String> attachmentUrls;

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

    public CommReportModel(EnrollmentRequest requestObj) {
        if (requestObj!=null) {
            setId(requestObj.getId());
            setRefNo(requestObj.getRefNo());
            setUserId(requestObj.getUserId());
            setReportTypeKey(requestObj.getReportTypeKey());
            setDescription(requestObj.getDescription());
            setLocation(requestObj.getLocation());
            setPriority(requestObj.getPriority());
            setHappensNearOrInHousehold(requestObj.getHappensNearOrInHousehold());
            setAttachments(requestObj.getAttachments());
            setRemarks(requestObj.getRemarks());
            setOthTitle(requestObj.getOthTitle());
            setAttachmentUrls(requestObj.getAttachmentUrls());

            setRemarks(requestObj.getRemarks());
            setAssigneeId(requestObj.getAssigneeId());
            setStatus(requestObj.getStatus());
            setCreatedDt(requestObj.getCreatedDt());
        }
    }
}