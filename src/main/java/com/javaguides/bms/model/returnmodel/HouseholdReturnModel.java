package com.javaguides.bms.model.returnmodel;

import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.PhaseEnum;
import com.javaguides.bms.enums.SystemStatusEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.UsersModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class HouseholdReturnModel {

    private String id;
    private String householdDesc;
    private String householdUniqKey;
    private Date createdDt;
    private String createdDtString;
    private String householdHead;
    private String ackMessage;
    private String refNo;

    private String firstNm;
    private String middleNm;
    private String lastNm;
    private String suffix;
    private String block;
    private String lot;
    private String street;
    private Integer phaseKey;
    private String phaseKeyString;

    private String homeAddress;
    private Integer status;
    private String statusString;
    private List<UsersModel> members;
    private String userId;

    private Integer userStatus;
    private String userStatusString;

    public Integer getMemberCount() {
        if (members!=null && !members.isEmpty()) return members.size();
        return 0;
    }

    public HouseholdReturnModel(HouseholdModel modelObj) {
        setId(modelObj.getId());
        setHouseholdDesc(modelObj.getHouseholdDesc());
        setHouseholdUniqKey(modelObj.getHouseholdUniqKey());
        setHouseholdHead(modelObj.getFullNm());
        setCreatedDt(modelObj.getCreatedDt());
        setCreatedDtString(DateUtil.getDateStringWithFormat(createdDt, DateFormatEnum.DT_FORMAT_5.getPattern()));
        setFirstNm(modelObj.getFirstNm());
        setMiddleNm(modelObj.getMiddleNm());
        setLastNm(modelObj.getLastNm());
        setSuffix(modelObj.getSuffix());
        setBlock(modelObj.getBlock());
        setLot(modelObj.getLot());
        setStreet(modelObj.getStreet());
        setPhaseKey(modelObj.getPhaseKey());
        setPhaseKeyString(PhaseEnum.getDesc2ByKey(phaseKey));
        setHomeAddress(modelObj.getHomeAddress());
        setStatus(modelObj.getStatus());
        setStatusString(SystemStatusEnum.getDscpByKey(status));
        setUserId(modelObj.getUserId());
        setUserStatus(modelObj.getUserStatus());
        setUserStatusString(modelObj.getUserStatusString());
    }

}
