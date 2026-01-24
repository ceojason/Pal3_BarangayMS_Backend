package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.model.AnnouncementModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DashboardReturnModel {

    private String paramCount1;
    private String paramCount2;
    private String paramCount3;

    private String paramLabel1;
    private String paramLabel2;
    private String paramLabel3;

    private List<AnnouncementModel> announcementList;
}
