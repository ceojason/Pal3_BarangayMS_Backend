package com.javaguides.bms.model;

import com.javaguides.bms.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_subjects")
public class SubjectsModel extends BaseModel {

    @Column(name = "SUBJECT_NM")
    public String subjectNm;

    @Column(name = "SUBJECT_TYPE")
    public String subjectType;

    @Column(name = "STRAND_KEY")
    public String strandKey;

    @Column(name = "SEMESTER")
    public String semester;

    @Column(name = "YEARLEVEL_KEY")
    public String yearlevelKey;

}
