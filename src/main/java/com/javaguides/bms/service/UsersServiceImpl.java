package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.KeyHasher;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.UsersReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import com.javaguides.bms.service.baseservice.SmsService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class UsersServiceImpl extends BaseServiceImpl implements UsersService {

    private final UsersJDBCRepository usersJDBCRepository;
    private final LoginJDBCRepository loginJDBCRepository;
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final SmsService smsService;
    private final AuditLogService auditLogService;
    static final String IS_REQUIRED_SUFFIX = " is required.";

    @Override
    public Page<UsersReturnModel> searchUsers(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<UsersModel> users = usersJDBCRepository.searchUsers(searchRequest, pageRequest);
        return users.map(UsersReturnModel::new);
    }

    @Override
    public UsersReturnModel validateEnrollment(EnrollmentRequest requestObj) {
        UsersModel modelObj = new UsersModel(requestObj);
        return validateObj(modelObj);
    }

    public UsersReturnModel validateObj(UsersModel modelObj) {
        List<String> errorList = new ArrayList<>();

        if (modelObj.getFirstNm()==null){
            errorList.add("First Name" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setFirstNm(modelObj.getFirstNm().trim().toUpperCase());
        }

        if (modelObj.getMiddleNm()!=null) {
            modelObj.setMiddleNm(modelObj.getMiddleNm().trim().toUpperCase());
        }

        if (modelObj.getLastNm()==null){
            errorList.add("Last Name" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setLastNm(modelObj.getLastNm().trim().toUpperCase());
        }

        if (modelObj.getSuffix()!=null) {
            modelObj.setSuffix(modelObj.getSuffix().trim().toUpperCase());
        }

        if (modelObj.getGender()==null) {
            errorList.add("Gender" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setGenderDscp(GenderEnum.getGenderDscpFromKeyStr(modelObj.getGender()));
        }

        if (modelObj.getHomeAddress()==null) {
            errorList.add("Home Address" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setHomeAddress(modelObj.getHomeAddress().trim().toUpperCase());
        }

        if (modelObj.getMobileNo()==null) {
            errorList.add("Mobile Number" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfOnlyNumber(modelObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
            String to = modelObj.getMobileNo();
            if (modelObj.getMobileNo().startsWith("0")) {
                modelObj.setFormattedMobileNo("+63" + to.substring(1));
            }
        }

        if (modelObj.getEmailAddress()!=null) {
            modelObj.setEmailAddress(modelObj.getEmailAddress().trim());
        }

        if (modelObj.getBirthDt()==null) {
            errorList.add("Birth date" + IS_REQUIRED_SUFFIX);
        }else{
            boolean isValidDt = DateUtil.checkValidDateFrom(modelObj.getBirthDt(), 10);
            if (isValidDt) {
                modelObj.setBirthDtString(DateUtil.getDateStringWithFormat(modelObj.getBirthDt(), DateFormatEnum.DT_FORMAT_1.getPattern()));
            }else{
                errorList.add("Invalid birth date.");
            }
        }

        if (modelObj.getBirthPlace()==null) {
            errorList.add("Birth place" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setBirthPlace(modelObj.getBirthPlace().toUpperCase().trim());
        }

        if (modelObj.getCivilStatusKey()==null) {
            errorList.add("Civil Status" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setCivilStatusString(CivilStatusEnum.getCivilStatusDescByKey(modelObj.getCivilStatusKey()));
        }

        if (modelObj.getPhaseKey()==null) {
            errorList.add("Purok" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setPhaseString(PhaseEnum.getDesc2ByKey(modelObj.getPhaseKey()));
        }

        if (modelObj.getHouseholdKey()!=null) {}

        if (modelObj.getOccupation()!=null) {
            modelObj.setOccupation(modelObj.getOccupation().toUpperCase().trim());
        }

        if (modelObj.getReligion()==null) {
            errorList.add("Religion" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setReligion(modelObj.getReligion().toUpperCase().trim());
        }

        if (modelObj.getIsRegisteredVoter()==null) {
            errorList.add("Is Registered Voter?" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setIsRegisteredVoterString(YesOrNoEnum.getDescByKey(modelObj.getIsRegisteredVoter()));
        }

        if (modelObj.getResidentClassKeys()==null || modelObj.getResidentClassKeys().isEmpty()) {
            errorList.add("Resident Classification" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setClassificationKey(modelObj.getClassificationKeyString());
        }

        if (modelObj.getDateEnrolled()==null) {
            modelObj.setDateEnrolled(new Date());
        }

        modelObj.setStatus(SystemStatusEnum.ACTIVE.getKey());

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        return new UsersReturnModel(modelObj);
    }

    @Override
    public UsersReturnModel saveEnrollment(EnrollmentRequest requestObj, HttpSession session) {
        UsersModel modelObj = new UsersModel(requestObj);
        validateObj(modelObj);
        modelObj.setRefNo(generateReferenceNumber(ServicesEnum.ADD_USERS.getCode()));
        UsersReturnModel returnObj = new UsersReturnModel(modelObj);

        boolean isSaved = usersJDBCRepository.saveEnrollment(modelObj)>0;
        if (isSaved) {
            try {
                saveLoginCreds(modelObj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.SAVED_SINGLE_SUFFIX,
                StringMessagesUtil.USER
                ));
        Object user = session.getAttribute("user");
        if (user instanceof LoginCreds currentUser) {
            returnObj.setCreatedBy(currentUser.getCd());
        }
        return returnObj;
    }

    public void saveLoginCreds(UsersModel modelObj) throws Exception {
        String defaultPass = KeyHasher.generateDefaultPassword();
        String defaultCd = KeyHasher.generateDefaultCd().toUpperCase();

        LoginCreds loginCreds = new LoginCreds();
        loginCreds.setUserId(modelObj.getId());
        loginCreds.setCd(defaultCd);
        loginCreds.setLoginStatus(SystemStatusEnum.LOGGED_OUT.getKey());
        loginCreds.setRole(SystemUserEnum.SYSTEM_USER.getKey());
        loginCreds.setSalt(KeyHasher.generateSalt());
        loginCreds.setPassword(KeyHasher.hashPassword(defaultPass, loginCreds.getSalt()));
        loginCreds.setUpdatedDt(new Date());

        loginJDBCRepository.saveLoginCreds(loginCreds);

        SmsModel sms = new SmsModel();
        sms.setRecipient(modelObj.getFormattedMobileNo());
        sms.setMessage("Hi, " + modelObj.getFirstNm()  + "! User ID: " + defaultCd + ", Password: " + defaultPass);
        //smsService.sendSms(sms);

        //saving notif logs
        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(modelObj.getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(SmsTypeEnum.NEW_USER_SMS.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);
    }

    @Override
    public UsersReturnModel update(EnrollmentRequest requestObj, HttpSession session) {
        UsersModel modelObj = new UsersModel(requestObj);
        validateObj(modelObj);

        usersJDBCRepository.updateUser(modelObj);
        try {
            checkCdAndPasswordThenSave(modelObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UsersReturnModel returnObj = new UsersReturnModel(modelObj);
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.USER
        ));
        return returnObj;
    }

    public void checkCdAndPasswordThenSave(UsersModel modelObj) throws Exception {
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

    @Override
    public UsersReturnModel deleteUser(String userId) {
        UsersReturnModel returnObj = new UsersReturnModel();
        List<String> errorList = new ArrayList<>();

        if (userId!=null) {
            Optional<UsersModel> user = usersJDBCRepository.findById(userId);
            if (user.isEmpty()) {
                errorList.add("User data was not found.");
            }else{
                returnObj.setFirstNm(user.get().getFirstNm());
                returnObj.setMiddleNm(user.get().getMiddleNm());
                returnObj.setLastNm(user.get().getLastNm());
            }

            List<LoginCreds> list = loginJDBCRepository.getUserById(userId);
            if (list.size()!=1) {
                errorList.add("An error occurred while processing the user's data.");
            }
        }

        if (!errorList.isEmpty()) {
            throwErrorMessages(errorList);
        }else{
            usersJDBCRepository.deleteById(userId);
            loginJDBCRepository.deleteByUserId(userId);
        }
        return returnObj;
    }

    @Override
    public UsersReturnModel findByUserId(String userId) {
        UsersReturnModel returnObj = new UsersReturnModel();
        Optional<UsersModel> user = usersJDBCRepository.findById(userId);
        List<LoginCreds> login = loginJDBCRepository.getUserById(userId);
        if (user.isPresent()) {
            UsersModel modelObj = user.get();

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

            returnObj.setBirthDt(modelObj.getBirthDt());
            returnObj.setBirthDtString(
                    DateUtil.getDateStringWithFormat(modelObj.getBirthDt(), DateFormatEnum.DT_FORMAT_1.getPattern())
            );

            returnObj.setBirthPlace(modelObj.getBirthPlace());
            returnObj.setGender(modelObj.getGender());
            returnObj.setGenderDscp(
                    GenderEnum.getGenderDscpFromKeyStr(modelObj.getGender())
            );

            returnObj.setCivilStatusKey(modelObj.getCivilStatusKey());
            returnObj.setCivilStatusString(
                    CivilStatusEnum.getCivilStatusDescByKey(modelObj.getCivilStatusKey())
            );

            returnObj.setMobileNo(modelObj.getMobileNo());
            returnObj.setFormattedMobileNo(modelObj.getFormattedMobileNo());
            returnObj.setHomeAddress(modelObj.getHomeAddress());
            returnObj.setEmailAddress(modelObj.getEmailAddress());
            returnObj.setOccupation(modelObj.getOccupation());
            returnObj.setReligion(modelObj.getReligion());

            returnObj.setResidentClassKeys(modelObj.getClassificationKeyList());
            returnObj.setClassificationTypeString(
                    modelObj.getClassificationKeyStringForDisplay()
            );

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

            returnObj.setIsRegisteredVoter(modelObj.getIsRegisteredVoter());
            returnObj.setIsRegisteredVoterString(
                    YesOrNoEnum.getDescByKey(modelObj.getIsRegisteredVoter())
            );

            returnObj.setRefNo(modelObj.getRefNo());
        }
        return returnObj;
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

