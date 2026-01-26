package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.KeyHasher;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemAdminJDBCRepository;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.SystemAdminModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.returnmodel.AdminReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@AllArgsConstructor
public class AdminEnrollmentServiceImpl extends BaseServiceImpl implements AdminEnrollmentService {

    private final SystemAdminJDBCRepository systemAdminJDBCRepository;
    private final LoginJDBCRepository loginJDBCRepository;

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

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        return modelObj;
    }

    @Override
    public AdminReturnModel findByUserId(String userId) {
        AdminReturnModel returnObj = new AdminReturnModel();
        Optional<SystemAdminModel> user = systemAdminJDBCRepository.findById(userId);
        List<LoginCreds> login = loginJDBCRepository.getUserById(userId);
        if (user.isPresent()) {
            SystemAdminModel modelObj = user.get();

            if (login!=null && !login.isEmpty()) {
                if (login.size()==1) {
                    returnObj.setLastLoginDt(login.get(0).getUpdatedDt());
                    returnObj.setLastLoginDtString(DateUtil.getDateStringWithFormat(returnObj.getLastLoginDt(), DateFormatEnum.DT_FORMAT_7.getPattern()));
                }
            }

            returnObj.setId(modelObj.getId());
            returnObj.setCd(modelObj.getCd());
            returnObj.setFirstNm(modelObj.getFirstNm());
            returnObj.setMiddleNm(modelObj.getMiddleNm());
            returnObj.setLastNm(modelObj.getLastNm());
            returnObj.setFullNm(modelObj.getFullNm());
            returnObj.setSuffix(modelObj.getSuffix());

            returnObj.setGender(modelObj.getGender());
            returnObj.setGenderDscp(
                    GenderEnum.getGenderDscpFromKeyStr(modelObj.getGender())
            );

            returnObj.setMobileNo(modelObj.getMobileNo());
            returnObj.setFormattedMobileNo(modelObj.getFormattedMobileNo());
            returnObj.setHomeAddress(modelObj.getHomeAddress());
            returnObj.setEmailAddress(modelObj.getEmailAddress());

            returnObj.setPhaseKey(modelObj.getPhaseKey());
            returnObj.setPhaseString(
                    PhaseEnum.getDesc2ByKey(modelObj.getPhaseKey())
            );

            returnObj.setDateEnrolled(modelObj.getDateEnrolled());
            returnObj.setDateEnrolledString(modelObj.getDateEnrolledString());

            returnObj.setStatus(modelObj.getStatus());
            returnObj.setStatusString(
                    SystemStatusEnum.getDscpByKey(modelObj.getStatus())
            );

            //returnObj.setRefNo(modelObj.getRefNo());
        }
        return returnObj;
    }

    @Override
    public AdminReturnModel update(EnrollmentRequest request) {
        SystemAdminModel modelObj = new SystemAdminModel(request);
        validate(modelObj);

        systemAdminJDBCRepository.updateAdmin(modelObj);
        try {
            checkCdAndPasswordThenSave(modelObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        AdminReturnModel returnObj = new AdminReturnModel();
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.USER
        ));
        return returnObj;
    }

    public void checkCdAndPasswordThenSave(SystemAdminModel modelObj) throws Exception {
        String userId = modelObj.getId();
        List<LoginCreds> login = loginJDBCRepository.getUserById(userId);

        if (login==null || login.isEmpty()) {
            throwErrorMessage("No user was found.");
        }
        else if (login.size()>1) {
            throwErrorMessage("An error occurred fetching user's data.");
        }
        else {
            LoginCreds user = login.get(0);
            if (modelObj.getCd()!=null) {
                user.setCd(modelObj.getCd());
            }

            if (modelObj.getPassword()!=null) {
                user.setPassword(KeyHasher.hashPassword(modelObj.getPassword(), user.getSalt()));
            }
            user.setUpdatedDt(new Date());
            loginJDBCRepository.update(user);
        }
    }

    private static final String PROFILE_IMAGE_DIR = "uploads/profile-images/";

    @Override
    public void saveProfileImage(String userId, MultipartFile file) {
        try {
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                throwErrorMessage("Invalid file type.");
            }

            Files.createDirectories(Paths.get(PROFILE_IMAGE_DIR));
            String filename = "user_" + userId + ".jpg";
            Path filePath = Paths.get(PROFILE_IMAGE_DIR, filename);
            Files.write(filePath, file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save profile image", e);
        }
    }

    @Override
    public Resource loadProfileImage(String userId) {
        try {
            Path filePath = Paths.get(PROFILE_IMAGE_DIR, "user_" + userId + ".jpg");
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Profile image not found.", e);
        }
    }

}
