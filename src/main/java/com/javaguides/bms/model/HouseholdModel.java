package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.model.basemodel.BaseModel;
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
}
