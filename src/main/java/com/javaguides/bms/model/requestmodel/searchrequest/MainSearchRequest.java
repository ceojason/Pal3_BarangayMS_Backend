package com.javaguides.bms.model.requestmodel.searchrequest;

import com.javaguides.bms.model.basemodel.SearchBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MainSearchRequest extends SearchBaseModel {

    private String firstNm;
    private String lastNm;

    private String recipient;
    private String refNo;

}
