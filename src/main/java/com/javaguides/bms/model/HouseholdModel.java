package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.PhaseEnum;
import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_household")
@TableAlias("th")
public class HouseholdModel extends BaseModel {

    @Column(name = "HOUSEHOLD_DESC")
    private String householdDesc;

    @Column(name = "HOUSEHOLD_UNIQ_KEY")
    private String householdUniqKey;

    @Column(name = "CREATED_DT")
    private Date createdDt;

    @Column(name = "UPDATED_DT")
    private Date updatedDt;

    @Transient
    private String householdHead;

    @Transient
    private String firstNm;

    @Transient
    private String middleNm;

    @Transient
    private String lastNm;

    @Transient
    private String suffix;

    @Transient
    private String block;

    @Transient
    private String lot;

    @Transient
    private String street;

    @Transient
    private Integer phaseKey;

    @Transient
    private String userId;

    @Transient
    private Integer userStatus;

    public String getUserStatusString() {
        return SystemStatusEnum.getDscpByKey(userStatus);
    }

    public String getHomeAddress() {
        String defaultBrgyNmCityAndProv = "PALIPARAN III DASMARIÑAS CITY, CAVITE";
        String defaultZipCd = "4114";
        boolean appendDefaultAddressString = true;

        StringBuilder address = new StringBuilder();
        if (block!=null) address.append(block).append(" ");
        if (lot!=null) address.append(lot).append(" ");
        if (street!=null) address.append(street).append(" ");
        if (phaseKey!=null) address.append(PhaseEnum.getDesc2ByKey(phaseKey)).append(" ");

        if (appendDefaultAddressString) {
            address.append(defaultBrgyNmCityAndProv).append(" ");
            address.append(defaultZipCd);
        }

        return address.toString();
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

    public String getHouseholdWithHead() {
        StringBuilder val = new StringBuilder();
        if (householdDesc!=null) {
            val.append(householdDesc);
        }
        if (lastNm!=null) {
            val.append(" - ").append(lastNm);
        }
        if (firstNm!=null) {
            val.append(", ")
                    .append(firstNm);
        }
        if (middleNm!=null) {
            val.append(", ")
                    .append(middleNm);
        }
        if (suffix!=null) {
            val.append(" ")
                    .append(suffix);
        }
        return val.toString();
    }

    public HouseholdModel(EnrollmentRequest requestObj) {
        if (requestObj!=null) {
            setId(requestObj.getId());
            setUserId(requestObj.getUserId());
            setHouseholdDesc(requestObj.getHouseholdDesc());
            setHouseholdUniqKey(requestObj.getHouseholdUniqKey());
            setStatus(requestObj.getStatus());
            setCreatedDt(requestObj.getCreatedDt());
        }
    }
}
