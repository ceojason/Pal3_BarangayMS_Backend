package com.javaguides.bms.model.requestmodel;

import com.javaguides.bms.model.basemodel.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String refNo;

}
