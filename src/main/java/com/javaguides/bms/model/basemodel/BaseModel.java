package com.javaguides.bms.model.basemodel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@MappedSuperclass
@Getter
@Setter
public class BaseModel {

    @Column(name = "ID")
    private String id;

//    @Column(name = "USER_ID")
//    private String userId;

    @Column(name = "STATUS")
    private Integer status;

//    @Column(name = "DEL")
    private String del;

//    @Column(name = "VERSION")
//    private String version;
    private List<String> errorList;
    public String ackMessage;
    public String createdBy;
    public String txnDscp;

    public BaseModel() {
        // Default constructor required by JPA
    }
}
