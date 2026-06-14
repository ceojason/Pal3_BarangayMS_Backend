package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.KeyHasher;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.*;
import com.javaguides.bms.model.*;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.ResetUserRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.UsersReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import com.javaguides.bms.service.baseservice.EmailService;
import com.javaguides.bms.service.baseservice.SmsService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UsersServiceImpl extends BaseServiceImpl implements UsersService {

    private final UsersJDBCRepository usersJDBCRepository;
    private final LoginJDBCRepository loginJDBCRepository;
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final HouseholdService householdService;
    private final HouseholdJDBCRepository householdJDBCRepository;
    private final SystemAdminJDBCRepository systemAdminJDBCRepository;
    private final SmsService smsService;
    private final EmailService emailService;
    private final AuditLogService auditLogService;
    private final ConfigService configService;
    static final String IS_REQUIRED_SUFFIX = " is required.";

    @Override
    public Page<UsersReturnModel> searchUsers(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<UsersModel> users = usersJDBCRepository.searchUsers(searchRequest, pageRequest);
        String configAddressPrefix = configService.getAddressConfigObj();
        users.stream().forEach(item -> {
            item.setAddressFromConfig(configAddressPrefix);
            item.setIsBrgyOfficial(item.getBrgyPositionKey()!=null ? YesOrNoEnum.YES.getKey() : YesOrNoEnum.NO.getKey());
        });
        return users.map(UsersReturnModel::new);
    }

    @Override
    public String createHouseholdForRegistration(EnrollmentRequest requestObj) {
        StringBuilder tempHousehold = new StringBuilder();
        if (requestObj.getLastNm()!=null && !requestObj.getLastNm().isEmpty()) {
            tempHousehold.append(requestObj.getLastNm().trim().toUpperCase());
        }
        tempHousehold.append(formatHouseholdUniqKey(requestObj));
        return tempHousehold.toString();
    }

    public String formatHouseholdUniqKey(EnrollmentRequest requestObj) {
        StringBuilder tempHousehold = new StringBuilder();
        if (requestObj.getBlock()!=null && !requestObj.getBlock().isEmpty()) {
            tempHousehold.append("_")
                    .append(requestObj.getBlock().trim().toUpperCase());
        }
        if (requestObj.getLot()!=null && !requestObj.getLot().isEmpty()) {
            tempHousehold.append("_")
                    .append(requestObj.getLot().trim().toUpperCase());
        }
        if (requestObj.getStreet()!=null && !requestObj.getStreet().isEmpty()) {
            tempHousehold.append("_")
                    .append(requestObj.getStreet().trim().toUpperCase());
        }
        if (requestObj.getPhaseKey()!=null) {
            tempHousehold.append("_")
                    .append(PhaseEnum.getDesc2ByKey(requestObj.getPhaseKey()));
        }
        return tempHousehold.toString();
    }

    @Override
    public UsersReturnModel validateEnrollment(EnrollmentRequest requestObj) {
        String tempUniqueKey = formatHouseholdUniqKey(requestObj);
        UsersModel modelObj = new UsersModel(requestObj);
        modelObj.setTempUniqueKey(tempUniqueKey);
        return validateObj(modelObj);
    }

    public UsersReturnModel validateObj(UsersModel modelObj) {
        boolean isAdd = modelObj.getId()==null || modelObj.getId().isEmpty();

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

        if (modelObj.getBlock()==null) {
            errorList.add("Address Block" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setBlock(modelObj.getBlock().toUpperCase());
        }

        if (modelObj.getLot()==null) {
            errorList.add("Address Lot" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setLot(modelObj.getLot().toUpperCase());
        }

        if (modelObj.getStreet()!=null) {
            modelObj.setStreet(modelObj.getStreet().toUpperCase());
        }

        if (isAdd) {
            if (modelObj.getIsHouseholdHead()==null) {
                errorList.add("Is Household Head" + IS_REQUIRED_SUFFIX);
            }else{
                modelObj.setIsHouseholdHeadString(YesOrNoEnum.getDescByKey(modelObj.getIsHouseholdHead()));
                if (modelObj.getIsHouseholdHead().equals(YesOrNoEnum.YES.getKey())) {
                    if (modelObj.getTempHouseholdForSave()==null) {
                        errorList.add("Household Description is required for household heads and cannot be emptied.");
                    }else{
                        List<HouseholdModel> hhList = householdJDBCRepository.findDuplicateHouseholdList(modelObj.getTempUniqueKey());
                        boolean hasAnExistingActiveHousehold = false;
                        if (hhList!=null && !hhList.isEmpty()) {
                            for (HouseholdModel hh : hhList) {
                                if (hh.getStatus().equals(SystemStatusEnum.ACTIVE.getKey())) {
                                    hasAnExistingActiveHousehold = true;
                                    break;
                                }
                            }
                            if (hasAnExistingActiveHousehold) {
                                if (hhList.size()>1) {
                                    errorList.add("There are multiple household saved in this address and one is currently on Active status. If you wish to proceed, you must update it to Inactive.");
                                    throwErrorMessages(errorList);
                                }else{
                                    errorList.add("Household under this address was already saved in the system and currently have a head. If you wish to proceed, you must update the existing household's status to Inactive.");
                                    throwErrorMessages(errorList);
                                }
                            }
                        }
                    }
                }else{
                    if (modelObj.getHouseholdKey()==null) {
                        errorList.add("Household" + IS_REQUIRED_SUFFIX);
                    }else{
                        Optional<HouseholdModel> household = householdJDBCRepository.findById(modelObj.getHouseholdKey());
                        household.ifPresent(householdModel -> modelObj.setTempHouseholdForSave(householdModel.getHouseholdDesc()));
                    }
                }
            }
        }else{
            Optional<UsersModel> savedUser = usersJDBCRepository.findById(modelObj.getId());
            if (savedUser!=null && savedUser.isPresent()) {
                if (modelObj.getIsHouseholdHead()==null) {
                    errorList.add("Is Household Head" + IS_REQUIRED_SUFFIX);
                }else{
                    if (modelObj.getIsHouseholdHead().equals(YesOrNoEnum.YES.getKey())) {
                        String householdId = modelObj.getHouseholdKey()!=null ? modelObj.getHouseholdKey() : savedUser.get().getHouseholdKey();
                        String uniqueKey = modelObj.getTempUniqueKey();
                        String userHouseholdKey = savedUser.get().getHouseholdKey();
                        boolean hasSavedHousehold = false;

                        Optional<HouseholdModel> tempHousehold = Optional.of(new HouseholdModel());
                        if (uniqueKey==null && householdId!=null) {
                            tempHousehold = householdJDBCRepository.findById(householdId);
                            uniqueKey = tempHousehold.map(HouseholdModel::getHouseholdUniqKey).orElse(null);
                            if (uniqueKey==null) throwErrorMessage("An error occurred. Transaction cannot be processed.");
                        }
                        List<HouseholdModel> hhList = householdJDBCRepository.findDuplicateHouseholdList(uniqueKey);

                        if (hhList!=null && !hhList.isEmpty()) {
                            for (HouseholdModel hh : hhList) {
                                if (hh.getId().equals(userHouseholdKey)) {
                                    hasSavedHousehold = true;
                                    break;
                                }
                            }
                        }else{
                            modelObj.setHouseholdKey(null);
                        }

                        if (hasSavedHousehold) {
                            modelObj.setHouseholdKey(userHouseholdKey);
                        }else{ //if the user is assigning to a new household and if he/she set as head
                            assert hhList!=null;
                            if (hhList.size()>1) {
                                errorList.add("There are multiple household saved in this address and one is currently on Active status. If you wish to proceed, you must update it to Inactive.");
                                throwErrorMessages(errorList);
                            }else if(hhList.size()==1){
                                errorList.add("Household under this address was already saved in the system and currently have a head. If you wish to proceed, you must update the existing household's status to Inactive.");
                                throwErrorMessages(errorList);
                            }
                        }
                    }else{
                        Optional<HouseholdModel> household = householdJDBCRepository.findById(modelObj.getHouseholdKey());
                        household.ifPresent(householdModel -> modelObj.setTempHouseholdForSave(householdModel.getHouseholdDesc()));
                    }
                }


            }else{
                throwErrorMessage("An error occurred. Transaction cannot be processed.");
            }
        }

        if (modelObj.getHouseholdKey()!=null) {}

        if (modelObj.getIsBrgyOfficial()!=null && YesOrNoEnum.YES.getKey().equals(modelObj.getIsBrgyOfficial())) {
            if (modelObj.getBrgyPositionKey()==null) {
                errorList.add("Please select a valid Barangay position from the list.");
            }else{
                modelObj.setBrgyPositionKeyString(BrgyPositionEnum.getDescByKey(modelObj.getBrgyPositionKey()));
            }
        }

        if (modelObj.getMobileNo()==null) {
            errorList.add("Mobile Number" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfOnlyNumber(modelObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(modelObj.getMobileNo(), 11, "Mobile Number", errorList);
//            String to = modelObj.getMobileNo();
//            if (modelObj.getMobileNo().startsWith("0")) {
//                modelObj.setFormattedMobileNo("+63" + to.substring(1));
//            }
        }

        if (modelObj.getEmailAddress()!=null) {
            modelObj.setEmailAddress(modelObj.getEmailAddress().trim());
        }

        if (modelObj.getBirthDt()==null) {
            errorList.add("Birth date" + IS_REQUIRED_SUFFIX);
        }else{
            boolean isValidDt = DateUtil.checkValidDateFrom(modelObj.getBirthDt(), 8);
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
        modelObj.setAddressFromConfig(configService.getAddressConfigObj());

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        return new UsersReturnModel(modelObj);
    }

    @Override
    public UsersReturnModel saveEnrollment(EnrollmentRequest requestObj) {
        UsersModel modelObj = new UsersModel(requestObj);
        modelObj.setTempUniqueKey(formatHouseholdUniqKey(requestObj));
        validateObj(modelObj);
        modelObj.setRefNo(generateReferenceNumber(ServicesEnum.ADD_USERS.getCode()));
        UsersReturnModel returnObj = new UsersReturnModel(modelObj);

        String householdId = "";
        if (modelObj.getIsHouseholdHead()!=null &&
            modelObj.getIsHouseholdHead().equals(YesOrNoEnum.YES.getKey()) &&
            modelObj.getTempHouseholdForSave()!=null
        ) {
            HouseholdModel household = new HouseholdModel();
            household.setStatus(SystemStatusEnum.ACTIVE.getKey());
            household.setHouseholdDesc(modelObj.getTempHouseholdForSave());
            household.setHouseholdUniqKey(modelObj.getTempUniqueKey());
            householdId = householdService.saveNewHousehold(household);
        } else {
            householdId = modelObj.getHouseholdKey();
        }

        modelObj.setHouseholdKey(householdId);
        boolean isSaved = usersJDBCRepository.saveEnrollment(modelObj)>0;
        if (isSaved && householdId!=null) {
            try {
                saveLoginCreds(modelObj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.SAVED_SINGLE_SUFFIX,
                StringMessagesUtil.RESIDENT
                ));
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

        String msg = "Hi, " + modelObj.getFirstNm()  + "! You have been successfully registered in Barangay eConnect System! Below are your temporary credentials.\n\n User ID: " + defaultCd + ", Password: " + defaultPass;

        SmsModel sms = new SmsModel();
        sms.setRecipient(modelObj.formattedMobileNo());
        sms.setMessage(msg);
        smsService.sendSms(sms);

        if (modelObj.getEmailAddress() != null) {
            emailService.sendSimpleEmailNotif(modelObj.getEmailAddress(), "User Registration", msg);
        }

        //saving notif logs
        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(modelObj.getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(LogsTypeEnum.NEW_USER_SMS.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsModel.setOtherDetail(modelObj.getTempHouseholdForSave());
        notifLogsModel.setMainActionStr(LogsTypeEnum.NEW_USER_SMS.getMainAction());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);
    }

    @Override
    public UsersReturnModel updateResident(EnrollmentRequest requestObj) {
        UsersModel modelObj = new UsersModel(requestObj);
        String tempUniqueKey = formatHouseholdUniqKey(requestObj);
        modelObj.setTempUniqueKey(tempUniqueKey);
        modelObj.setClassificationKey(modelObj.getClassificationKeyString());

        String householdId = "";
        if (modelObj.getIsHouseholdHead()!=null && YesOrNoEnum.YES.getKey().equals(modelObj.getIsHouseholdHead()) && modelObj.getHouseholdKey()==null) {
            HouseholdModel household = new HouseholdModel();
            household.setStatus(SystemStatusEnum.ACTIVE.getKey());
            household.setHouseholdDesc(modelObj.getTempHouseholdForSave());
            household.setHouseholdUniqKey(modelObj.getTempUniqueKey());
            householdId = householdService.saveNewHousehold(household);
            modelObj.setHouseholdKey(householdId);
        }
        if (modelObj.getIsHouseholdHead()!=null && YesOrNoEnum.YES.getKey().equals(modelObj.getIsHouseholdHead()) && modelObj.getHouseholdKey()!=null) {
            List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(List.of(modelObj.getHouseholdKey()));
            UsersModel currentHead = new UsersModel();
            if (members!=null && !members.isEmpty()) {
                for (UsersModel m : members) {
                    if (m.getIsHouseholdHead().equals(YesOrNoEnum.YES.getKey())) {
                        currentHead = m;
                        break;
                    }
                }
            }
            if (currentHead.getId()!=null && !currentHead.getId().equals(modelObj.getId())) {
                usersJDBCRepository.updateIsHouseholdHeadById(currentHead.getId(), YesOrNoEnum.NO.getKey());
            }
        }

        usersJDBCRepository.updateUser(modelObj);


        String msg = "Hi, " + modelObj.getFirstNm()  + "! You details has been successfully updated in Barangay eConnect System.";

        SmsModel sms = new SmsModel();
        sms.setRecipient(modelObj.formattedMobileNo());
        sms.setMessage(msg);
        smsService.sendSms(sms);

        if (modelObj.getEmailAddress() != null) {
            emailService.sendSimpleEmailNotif(modelObj.getEmailAddress(), "Resident Detail - Update", msg);
        }

        //saving notif logs
        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(modelObj.getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(LogsTypeEnum.RESIDENT_UPDATE.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsModel.setOtherDetail(modelObj.getFullNm());
        notifLogsModel.setMainActionStr(LogsTypeEnum.RESIDENT_UPDATE.getSecAction());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);




        UsersReturnModel returnObj = new UsersReturnModel(modelObj);
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.RESIDENT_DTLS
        ));
        returnObj.setRefNo(generateReferenceNumber(null));
        return returnObj;
    }

    @Override
    public UsersReturnModel update(EnrollmentRequest requestObj) {
        UsersModel modelObj = new UsersModel(requestObj);
        validateObj(modelObj);

        Optional<LoginCreds> loginCreds = loginJDBCRepository.getUserByCd(modelObj.getCd());
        if (loginCreds.isPresent()) {
            throwErrorMessage("User ID was already taken. Please try again.");
        }
        usersJDBCRepository.updateUser(modelObj);
        try {
            checkCdAndPasswordThenSave(modelObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UsersReturnModel returnObj = new UsersReturnModel(modelObj);
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.RESIDENT_DTLS
        ));
        return returnObj;
    }

    public void checkCdAndPasswordThenSave(UsersModel modelObj) throws Exception {
        String userId = modelObj.getId();
        Optional<LoginCreds> loginObj = loginJDBCRepository.getUserById(userId);

        if (loginObj.isEmpty()) {
            throwErrorMessage("No user was found.");
        }else {
            LoginCreds user = loginObj.get();
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

            Optional<LoginCreds> loginObj = loginJDBCRepository.getUserById(userId);
            if (loginObj.isEmpty()) {
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
    public UsersReturnModel findUserByRequest(ResetUserRequest requestObj) {
        UsersModel tempUser = new UsersModel();
        List<String> errorList = new ArrayList<>();
        if (requestObj.getFirstNm()==null){
            errorList.add("First Name" + IS_REQUIRED_SUFFIX);
        }else{
            requestObj.setFirstNm(requestObj.getFirstNm().trim().toUpperCase());
        }

        if (requestObj.getLastNm()==null){
            errorList.add("Last Name" + IS_REQUIRED_SUFFIX);
        }else{
            requestObj.setLastNm(requestObj.getLastNm().trim().toUpperCase());
        }

        if (requestObj.getMobileNo()==null) {
            errorList.add("Mobile Number" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfOnlyNumber(requestObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(requestObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(requestObj.getMobileNo(), 11, "Mobile Number", errorList);
            String to = requestObj.getMobileNo();
        }

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        MainSearchRequest sr = new MainSearchRequest();
        sr.setFirstNm(requestObj.getFirstNm());
        sr.setLastNm(requestObj.getLastNm());
        sr.setMobileNo(requestObj.getMobileNo());

        UsersModel modelObj = usersJDBCRepository.findUserInResetNoSession(sr);
        boolean noUserFound = false;
        if (modelObj==null || modelObj.getId()==null) {
            noUserFound = true;
        }else{
            tempUser.setId(modelObj.getId());
            tempUser.setFirstNm(modelObj.getFirstNm()!=null ? modelObj.getFirstNm() : null);
            tempUser.setMiddleNm(modelObj.getMiddleNm()!=null ? modelObj.getMiddleNm() : null);
            tempUser.setLastNm(modelObj.getLastNm()!=null ? modelObj.getLastNm() : null);
            tempUser.setSuffix(modelObj.getSuffix()!=null ? modelObj.getSuffix() : null);
            tempUser.setEmailAddress(modelObj.getEmailAddress()!=null ? modelObj.getEmailAddress() : null);
        }

        if (noUserFound) {
            SystemAdminModel adminObj = systemAdminJDBCRepository.findUserInResetNoSession(sr); //check if the user is admin
            if (adminObj!=null && adminObj.getId()!=null) {
                noUserFound = false;
                tempUser.setId(adminObj.getId());
                tempUser.setFirstNm(adminObj.getFirstNm()!=null ? adminObj.getFirstNm() : null);
                tempUser.setMiddleNm(adminObj.getMiddleNm()!=null ? adminObj.getMiddleNm() : null);
                tempUser.setLastNm(adminObj.getLastNm()!=null ? adminObj.getLastNm() : null);
                tempUser.setSuffix(adminObj.getSuffix()!=null ? adminObj.getSuffix() : null);
                tempUser.setEmailAddress(adminObj.getEmailAddress()!=null ? adminObj.getEmailAddress() : null);
            }
        }

        if (noUserFound) throwErrorMessage("No user was found.");
        return new UsersReturnModel(tempUser);
    }

    @Override
    public UsersReturnModel resetNoSession(EnrollmentRequest requestObj) {
        List<String> errorList = new ArrayList<>();
        requestObj.setHasNoSession(true);
        if (requestObj.getFirstNm()==null){
            errorList.add("First Name" + IS_REQUIRED_SUFFIX);
        }else{
            requestObj.setFirstNm(requestObj.getFirstNm().trim().toUpperCase());
        }

        if (requestObj.getLastNm()==null){
            errorList.add("Last Name" + IS_REQUIRED_SUFFIX);
        }else{
            requestObj.setLastNm(requestObj.getLastNm().trim().toUpperCase());
        }

        if (requestObj.getMobileNo()==null) {
            errorList.add("Mobile Number" + IS_REQUIRED_SUFFIX);
        }else{
            checkIfOnlyNumber(requestObj.getMobileNo(), "Mobile Number", errorList);
            maxStringCharCounter(requestObj.getMobileNo(), 11, "Mobile Number", errorList);
            minStringCharCounter(requestObj.getMobileNo(), 11, "Mobile Number", errorList);
            String to = requestObj.getMobileNo();
            if (requestObj.getMobileNo().startsWith("0")) {
                requestObj.setFormattedMobileNo("+63" + to.substring(1));
            }
        }

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        MainSearchRequest sr = new MainSearchRequest();
        sr.setFirstNm(requestObj.getFirstNm());
        sr.setLastNm(requestObj.getLastNm());
        sr.setBirthDt(requestObj.getBirthDt());
        sr.setMobileNo(requestObj.getMobileNo());

        UsersModel modelObj = usersJDBCRepository.findUserInResetNoSession(sr);
        boolean noUserFound = false;
        if (modelObj==null || modelObj.getId()==null) {
            noUserFound = true;
        }else{
            requestObj.setId(modelObj.getId());
            requestObj.setMiddleNm(modelObj.getMiddleNm()!=null ? modelObj.getMiddleNm() : null);
            requestObj.setEmailAddress(modelObj.getEmailAddress()!=null ? modelObj.getEmailAddress() : null);
        }

        if (noUserFound) {
            SystemAdminModel adminObj = systemAdminJDBCRepository.findUserInResetNoSession(sr); //check if the user is admin
            if (adminObj!=null && adminObj.getId()!=null) {
                noUserFound = false;
                requestObj.setId(adminObj.getId());
                requestObj.setMiddleNm(adminObj.getMiddleNm()!=null ? adminObj.getMiddleNm() : null);
                requestObj.setEmailAddress(adminObj.getEmailAddress()!=null ? adminObj.getEmailAddress() : null);
            }
        }

        if (noUserFound) throwErrorMessage("No user was found.");
        return reset(requestObj);
    }

    @Override
    public UsersReturnModel reset(EnrollmentRequest requestObj) {
        UsersModel modelObj = new UsersModel(requestObj);
        Optional<LoginCreds> login = loginJDBCRepository.getUserById(requestObj.getId());
        if (login.isEmpty()) {
            throwErrorMessage("No user was found.");
        }else {
            LoginCreds loginObj = login.get();
            String defaultPass = KeyHasher.generateDefaultPassword();
            String defaultCd = KeyHasher.generateDefaultCd().toUpperCase();

            loginObj.setUserId(requestObj.getId());
            try {
                loginObj.setPassword(KeyHasher.hashPassword(defaultPass, loginObj.getSalt()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            loginObj.setCd(defaultCd);
            loginJDBCRepository.update(loginObj);

            Optional<UsersModel> usersModel = usersJDBCRepository.findById(loginObj.getUserId());

            String msg = "Hi, " + modelObj.getFirstNm()  + "! Your account was successfully reset. User ID: " + defaultCd + ", Password: " + defaultPass;
            SmsModel sms = new SmsModel();
            sms.setRecipient(modelObj.formattedMobileNo());
            sms.setMessage(msg);
            smsService.sendSms(sms);

            NotifLogsModel notifLogsModel = new NotifLogsModel(); //saving notif logs
            notifLogsModel.setRefNo(generateReferenceNumber(null));
            notifLogsModel.setUserId(usersModel.map(BaseModel::getId).orElse(null));
            notifLogsModel.setMessage(sms.getMessage());
            notifLogsModel.setRecipient(modelObj.getFullNm());
            notifLogsModel.setIsSmsEmail(ChannelEnum.ALL.getKey());
            notifLogsModel.setSentDt(new Date());
            notifLogsModel.setType(LogsTypeEnum.RESET_USER.getKey());
            notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
            notifLogsModel.setOtherDetail(LogsTypeEnum.RESET_USER.getMainAction());
            notifLogsModel.setMainActionStr(LogsTypeEnum.RESET_USER.getSecAction());
            notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);

            if (requestObj.getEmailAddress()!=null) {
                emailService.sendSimpleEmailNotif(modelObj.getEmailAddress(), "Reset User Confirmation", msg);
            }
        }

        UsersReturnModel returnObj = new UsersReturnModel();
        StringBuilder ackMsg = new StringBuilder()
                .append(StringMessagesUtil.formatMsgString(
                        StringMessagesUtil.RESET_SINGLE_SUFFIX,
                        StringMessagesUtil.RESIDENT));
        if (requestObj.getHasNoSession()!=null && requestObj.getHasNoSession()) {
            ackMsg.append(" ").append("Please check your new login details through SMS or Email.");
        }

        returnObj.setAckMessage(ackMsg.toString());
        return returnObj;
    }

    @Override
    public UsersReturnModel findByUserId(String userId) {
        UsersReturnModel returnObj = new UsersReturnModel();
        Optional<UsersModel> user = usersJDBCRepository.findById(userId);
        Optional<LoginCreds> loginObj = loginJDBCRepository.getUserById(userId);
        if (user.isPresent()) {
            UsersModel modelObj = user.get();
            modelObj.setAddressFromConfig(configService.getAddressConfigObj());

            if (loginObj.isPresent()) {
                returnObj.setLastLoginDt(loginObj.get().getUpdatedDt());
                returnObj.setLastLoginDtString(DateUtil.getDateStringWithFormat(returnObj.getLastLoginDt(), DateFormatEnum.DT_FORMAT_7.getPattern()));
            }

            returnObj.setId(modelObj.getId());
            returnObj.setCd(modelObj.getCd());
            returnObj.setFirstNm(modelObj.getFirstNm());
            returnObj.setMiddleNm(modelObj.getMiddleNm());
            returnObj.setLastNm(modelObj.getLastNm());
            returnObj.setFullNm(modelObj.getFullNm());
            returnObj.setSuffix(modelObj.getSuffix());

            returnObj.setBrgyPositionKey(modelObj.getBrgyPositionKey());
            returnObj.setIsBrgyOfficial(modelObj.getBrgyPositionKey()!=null ? YesOrNoEnum.YES.getKey() : YesOrNoEnum.NO.getKey());
            returnObj.setBlock(modelObj.getBlock());
            returnObj.setLot(modelObj.getLot());
            returnObj.setIsHouseholdHead(modelObj.getIsHouseholdHead());
            returnObj.setIsHouseholdHeadString(YesOrNoEnum.getDescByKey(modelObj.getIsHouseholdHead()));
            returnObj.setStreet(modelObj.getStreet());
            returnObj.setHouseholdKey(modelObj.getHouseholdKey());

            Optional<HouseholdModel> household = householdJDBCRepository.findById(modelObj.getHouseholdKey());
            household.ifPresent(householdModel -> returnObj.setTempHouseholdForSave(householdModel.getHouseholdDesc()));

            List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(List.of(modelObj.getHouseholdKey()));
            List<String> memberNames = new ArrayList<>();
            if (members!=null && !members.isEmpty()) {
                memberNames = members.stream().map(UsersModel::getFullNm).toList();
                String tempMembers = members.stream()
                        .map(UsersModel::getFullNm2)
                        .collect(Collectors.joining(", "));
                returnObj.setHouseholdMembersString(tempMembers);
            }
            returnObj.setHouseholdMembers(memberNames);

            returnObj.setIsHouseholdHead(modelObj.getIsHouseholdHead());

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
            returnObj.setFormattedMobileNo(modelObj.formattedMobileNo());
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

