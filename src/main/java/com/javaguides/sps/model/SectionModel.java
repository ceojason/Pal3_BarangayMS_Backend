package com.javaguides.sps.model;

import com.javaguides.sps.customannotations.TableAlias;
import com.javaguides.sps.model.basemodel.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_sections")
@TableAlias("tss")
public class SectionModel extends BaseModel {

    @Column(name = "SECTION_NM")
    private String sectionNm;

    @Column(name = "YEARLEVEL_KEY")
    private String yearlevelKey;

    @Column(name = "STRAND_KEY")
    private String strandKey;

    @Column(name = "ADVISER_ID")
    private String adviserId;

    @Column(name = "STATUS")
    private Integer status;

    public String getSectionNmStrand() {
        if (strandKey!=null && sectionNm!=null) return strandKey + " "  + sectionNm;
        return sectionNm;
    }
}
