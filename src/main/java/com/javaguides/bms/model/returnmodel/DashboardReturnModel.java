package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.ConfigModel;
import com.javaguides.bms.model.NotifLogsModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class DashboardReturnModel {

    private String paramCount1;
    private String paramCount2;
    private String paramCount3;
    private String paramCount4;
    private String paramCount5;

    private String paramLabel1;
    private String paramLabel2;
    private String paramLabel3;
    private String paramLabel4;
    private String paramLabel5;

    private List<NotifLogsModel> logsList;
    private List<KeyValueModelStr> hotlineList;

    private Integer systemActiveCount;

    private List<AnnouncementModel> announcementList;
}
