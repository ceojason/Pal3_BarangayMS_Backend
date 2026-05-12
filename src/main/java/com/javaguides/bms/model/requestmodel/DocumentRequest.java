package com.javaguides.bms.model.requestmodel;

import com.javaguides.bms.model.basemodel.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest extends BaseModel {

    private String userId;
    private Integer documentType;
    private String purpose;
    private Date dateRequested;
    private String body;
    private String header;
    private String footer;

    private Integer docuCategoryKey;
    private Integer docuSubCategoryKey;
    private BigDecimal processFee;
    private Integer purposeKey;
    private String othPurpose;

    private String refNo;

}
