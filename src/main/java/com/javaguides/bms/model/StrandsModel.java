package com.javaguides.bms.model;

import com.javaguides.bms.model.basemodel.BaseModel;
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
@Table(name="tbl_strands")
public class StrandsModel extends BaseModel {

    @Column(name = "TRACK_KEY")
    public String trackKey;

    @Column(name = "STRAND_KEY")
    public String strandKey;

    @Column(name = "STRAND_DSCP")
    public String strandDscp;

    public String getTrackStrandDscp() {
        if (trackKey!=null && strandDscp!=null) return trackKey+" - "+strandDscp;
        return strandDscp;
    }
}
