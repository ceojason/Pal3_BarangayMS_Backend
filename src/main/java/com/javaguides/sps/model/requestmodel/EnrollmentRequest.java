package com.javaguides.sps.model.requestmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    //student proper
    private String id;
    private String lrn;
    private String firstNm;
    private String middleNm;
    private String lastNm;
    private Date bday;
    private String gender;
    private String homeAddress;
    private String mobileNo;
    private String emailAddress;
    private String guardianFirstNm;
    private String guardianMiddleNm;
    private String guardianLastNm;
    private String guardianMobileNo;
    private String yearlevelKey;
    private String strandKey;
    private String sectionId;
    private Integer status;
    private Integer enrolleeTypeKey;
    private List<String> enrolledSubjects;
    private Date dateEnrolled;

}
