package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.helper.NumberFormatterUtil;
import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_process_fee")
@TableAlias("tpf")
public class ProcessFeeModel extends BaseModel {

    @Column(name = "DOC_SUB_CAT_KEY")
    private Integer docSubCatKey;

    @Column(name = "PROCESS_FEE")
    private BigDecimal processFee;


    public String getProceeFeeString() {
        return NumberFormatterUtil.format(processFee);
    }
}
