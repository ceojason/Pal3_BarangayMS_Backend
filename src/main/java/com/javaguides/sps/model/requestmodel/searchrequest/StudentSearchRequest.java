package com.javaguides.sps.model.requestmodel.searchrequest;

import com.javaguides.sps.model.basemodel.SearchBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentSearchRequest extends SearchBaseModel {

    private String lrn;
    private String firstNm;
    private String lastNm;
    private String emailAddress;

}
