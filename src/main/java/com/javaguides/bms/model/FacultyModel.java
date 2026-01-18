package com.javaguides.bms.model;

import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_faculty")
public class FacultyModel extends BaseModel {

    @Column(name = "FIRST_NM")
    private String firstNm;

    @Column(name = "MIDDLE_NM")
    private String middleNm;

    @Column(name = "LAST_NM")
    private String lastNm;

    @Column(name = "BIRTHDAY")
    private Date bday;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "HOME_ADDRESS")
    private String homeAddress;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "ROLE_KEY")
    private String roleKey;

    @Column(name = "YEAR_LEVEL_ID")
    private String yearlevelKey;

    @Column(name = "SECTION_ID")
    private String sectionId;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "DATE_ENROLLED")
    private Integer dateEnrolled;

}
