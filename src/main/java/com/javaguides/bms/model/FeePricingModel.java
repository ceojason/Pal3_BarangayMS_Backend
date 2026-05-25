package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.helper.NumberFormatterUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_process_fee")
@TableAlias("tfp")
public class FeePricingModel extends BaseModel {

    @Column(name = "DOC_SUB_CAT_KEY")
    private Integer docSubCatKey;

    @Column(name = "PROCESS_FEE")
    private BigDecimal processFee;

    @Transient
    private String docSubCatKeyString;

    @Transient
    private Integer docCatKey;

    @Transient
    private String docCatKeyString;

    public String formattedProcessFeeToString() {
        return NumberFormatterUtil.format(processFee);
    }

}
