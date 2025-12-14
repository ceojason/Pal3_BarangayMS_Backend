package com.javaguides.sps.model;

import com.javaguides.sps.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EnrolledSubjectsModel extends BaseModel {

    @Column(name = "LRN")
    private String lrn;

    @Column(name = "SUBJECT_ID")
    private String subjectId;

    @Column(name = "FIRST_GRADE")
    private String firstGrade;

    @Column(name = "SECOND_GRADE")
    private String secondGrade;

    @Column(name = "VERSION")
    private Integer version;
}
