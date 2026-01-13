package com.javaguides.sps.service;

import com.javaguides.sps.enums.DateFormatEnum;
import com.javaguides.sps.enums.GenderEnum;
import com.javaguides.sps.helper.DateUtil;
import com.javaguides.sps.helper.StringMessagesUtil;
import com.javaguides.sps.model.SystemAdminModel;
import com.javaguides.sps.model.requestmodel.EnrollmentRequest;
import com.javaguides.sps.service.baseservice.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminEnrollmentServiceImpl extends BaseServiceImpl implements AdminEnrollmentService {

    @Override
    public SystemAdminModel validateEnrollment(EnrollmentRequest requestObj) {
        SystemAdminModel adminModel = new SystemAdminModel(requestObj);
        return validate(adminModel);
    }

    public SystemAdminModel validate(SystemAdminModel modelObj) {
        List<String> errorList = new ArrayList<>();

        if (modelObj.getFirstNm()==null){
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.FIRST_NM));
        }else{
            modelObj.setFirstNm(modelObj.getFirstNm().trim().toUpperCase());
        }

        if (modelObj.getMiddleNm()!=null) {
            checkIfAlphanumericSp(modelObj.getMiddleNm(), "Middle Name", errorList);
            modelObj.setMiddleNm(modelObj.getMiddleNm().trim().toUpperCase());
        }

        if (modelObj.getLastNm()==null){
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.LAST_NM));
        }else{
            modelObj.setLastNm(modelObj.getLastNm().trim().toUpperCase());
        }

        if (modelObj.getGender()==null) {
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.GENDER));
        }else{
            modelObj.setGenderDscp(GenderEnum.getGenderDscpFromKeyStr(modelObj.getGender()));
        }

        if (modelObj.getHomeAddress()==null) {
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.HOME_ADDRESS));
        }else{
            modelObj.setHomeAddress(modelObj.getHomeAddress().trim().toUpperCase());
        }

        if (modelObj.getMobileNo()==null) {
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.MOBILE_NO));
        }else{
            checkIfOnlyNumber(modelObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
        }

        if (modelObj.getEmailAddress()==null) {
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.EMAIL));
        }else{
            modelObj.setEmailAddress(modelObj.getEmailAddress().trim());
        }

        if (modelObj.getBday()==null) {
            errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.IS_REQUIRED_SUFFIX, StringMessagesUtil.BIRTHDAY));
        }else{
            boolean isValidDt = DateUtil.checkValidDateFrom(modelObj.getBday(), 10);
            if (isValidDt) {
                modelObj.setBdayDscp(DateUtil.getDateStringWithFormat(modelObj.getBday(), DateFormatEnum.DT_FORMAT_1.getPattern()));
            }else{
                errorList.add(StringMessagesUtil.formatMsgString(StringMessagesUtil.INVALID_OBJ, "Birth Date"));
            }
        }

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        return modelObj;
    }

}
