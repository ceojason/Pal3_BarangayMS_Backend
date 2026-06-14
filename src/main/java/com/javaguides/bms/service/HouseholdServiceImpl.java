package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.HouseholdJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.HouseholdModel;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.HouseholdReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import com.javaguides.bms.service.baseservice.EmailService;
import com.javaguides.bms.service.baseservice.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class HouseholdServiceImpl extends BaseServiceImpl implements HouseholdService {

    private final HouseholdJDBCRepository householdJDBCRepository;
    private final UsersJDBCRepository usersJDBCRepository;
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final SmsService smsService;
    private final EmailService emailService;

    @Override
    public String saveNewHousehold(HouseholdModel household) {
        household.setCreatedDt(new Date());
        return householdJDBCRepository.save(household);
    }

    @Override
    public Page<HouseholdReturnModel> search(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<HouseholdModel> households = householdJDBCRepository.search(searchRequest, pageRequest);
        Map<String, List<UsersModel>> membersMap = new HashMap<>();

        if (!households.isEmpty()) {
            List<String> householdIds = households.stream().map(BaseModel::getId).toList();

            if (!householdIds.isEmpty()) {
                List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(householdIds);

                if (members!=null && !members.isEmpty()) {
                    for (UsersModel u : members) {
                        membersMap.computeIfAbsent(u.getHouseholdKey(), m -> new ArrayList<>()).add(u);
                    }
                }
            }
        }

        return households.map(household -> {
            HouseholdReturnModel returnObj = new HouseholdReturnModel(household);
            List<UsersModel> members =
                    membersMap.getOrDefault(household.getId(), Collections.emptyList());

            UsersModel householdHead = members.stream()
                    .filter(m -> m.getIsHouseholdHead()!=null && m.getIsHouseholdHead()==0)
                    .findFirst()
                    .orElse(null);
            returnObj.setUserId(householdHead!=null ? householdHead.getId() : null);
            returnObj.setHouseholdHead(householdHead!=null ? householdHead.getFullNm() : null);
            returnObj.setUserStatus(householdHead!=null ? householdHead.getStatus() : null);
            if (returnObj.getUserStatus()!=null) returnObj.setUserStatusString(SystemStatusEnum.getDscpByKey(returnObj.getUserStatus()));
            returnObj.setMembers(members);

            return returnObj;
        });
    }

    @Override
    public List<KeyValueModelStr> findAllActiveHouseholdForRegistration(String block, String lot, String street, Integer phaseKey) {
        //int status = SystemStatusEnum.ACTIVE.getKey();
        List<HouseholdModel> householdList = householdJDBCRepository.findDuplicateHouseholdList(formatHouseholdUniqueKey(block, lot, street, phaseKey));
        List<KeyValueModelStr> returnList = new ArrayList<>();
        if (householdList!=null && !householdList.isEmpty()) {
            for (HouseholdModel modelObj : householdList) {
                returnList.add(new KeyValueModelStr(modelObj.getId(), modelObj.getHouseholdWithHead()));
            }
            returnList.sort(Comparator.comparing(KeyValueModelStr::getValue));
        }
        return returnList;
    }

    public String formatHouseholdUniqueKey(String block, String lot, String street, Integer phaseKey) {
        StringBuilder tempHousehold = new StringBuilder();
        if (block!=null && !block.isEmpty()) {
            tempHousehold.append("_")
                    .append(block.trim().toUpperCase());
        }
        if (lot!=null && !lot.isEmpty()) {
            tempHousehold.append("_")
                    .append(lot.trim().toUpperCase());
        }
        if (street!=null && !street.isEmpty()) {
            tempHousehold.append("_")
                    .append(street.trim().toUpperCase());
        }
        if (phaseKey!=null) {
            tempHousehold.append("_")
                    .append(PhaseEnum.getDesc2ByKey(phaseKey));
        }
        return tempHousehold.toString();
    }

    @Override
    public List<KeyValueModelStr> findMembersById(String id) {
        List<KeyValueModelStr> returnList = new ArrayList<>();
        if (id!=null && !id.isEmpty()) {
            List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(List.of(id));
            if (members!=null) {
                for (UsersModel u : members) {
                    returnList.add(new KeyValueModelStr(u.getId(), u.getFullNm()));
                }
            }
        }

        return returnList;
    }

    @Override
    public HouseholdReturnModel update(EnrollmentRequest requestObj) {
        HouseholdModel modelObj = new HouseholdModel(requestObj);
        modelObj.setErrorList(new ArrayList<>());

        boolean hasChanges = false;

        Optional<UsersModel> newHead = Optional.of(new UsersModel());
        UsersModel oldHead = new UsersModel();
        List<HouseholdModel> otherHouseholdList = new ArrayList<>();

        if (modelObj.getId()==null || modelObj.getId().isEmpty()) {
            modelObj.getErrorList().add("Invalid household. Transaction cannot be processed.");
        }else{
            Optional<HouseholdModel> tempObj = householdJDBCRepository.findById(modelObj.getId());
            if (tempObj.isEmpty()) {
                modelObj.getErrorList().add("Invalid household. Transaction cannot be processed.");
            }else{
                modelObj.setHouseholdDesc(tempObj.get().getHouseholdDesc());
                modelObj.setCreatedDt(tempObj.get().getCreatedDt());
                modelObj.setHouseholdUniqKey(tempObj.get().getHouseholdUniqKey());

                ////////// getting if it has duplicate unique key value ////////////
                if (modelObj.getStatus()!=null && SystemStatusEnum.ACTIVE.getKey().equals(modelObj.getStatus())) {
                    String uniqueKey = tempObj.get().getHouseholdUniqKey();
                    List<HouseholdModel> tempList = householdJDBCRepository.findDuplicateHouseholdList(uniqueKey);

                    if (tempList!=null && !tempList.isEmpty() && tempList.size()>1) {
                        otherHouseholdList = tempList.stream().filter(h -> !h.getId().equals(tempObj.get().getId())).toList();
                    }
                }
               /////////////////////////////////////////////////////////////////

                if (!tempObj.get().getStatus().equals(modelObj.getStatus())) {
                    hasChanges = true;
                }

                List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(List.of(tempObj.get().getId()));
                if (members!=null && !members.isEmpty()) {
                    oldHead = members.stream().filter(m -> m.getIsHouseholdHead().equals(YesOrNoEnum.YES.getKey())).findFirst().orElse(null);

                    if (oldHead==null || oldHead.getId()==null) {
                        newHead = usersJDBCRepository.findById(modelObj.getUserId());
                        if (newHead.isEmpty()) {
                            throwErrorMessage("An error occurred upon assigning a new household head.");
                        }
                        hasChanges = true;
                    }
                    if (oldHead!=null && oldHead.getId()!=null && !oldHead.getId().equals(modelObj.getUserId())) {
                        newHead = usersJDBCRepository.findById(modelObj.getUserId());
                        if (newHead.isEmpty()) {
                            throwErrorMessage("An error occurred upon assigning a new household head.");
                        }
                        hasChanges = true;
                    }else{
                        newHead = usersJDBCRepository.findById(modelObj.getUserId());
                    }
                }
//                else{
//                    modelObj.getErrorList().add("Invalid household. Transaction cannot be processed.");
//                }
            }
        }

        if (!hasChanges) modelObj.getErrorList().add("No changes were made.");
        if (modelObj.getErrorList()!=null && !modelObj.getErrorList().isEmpty()) {
            throwErrorMessages(modelObj.getErrorList());
        }

        householdJDBCRepository.update(modelObj);
        if (newHead.isPresent()) {
            usersJDBCRepository.updateIsHouseholdHeadById(newHead.get().getId(), YesOrNoEnum.YES.getKey());

            if (oldHead!=null && oldHead.getId()!=null) {
                usersJDBCRepository.updateIsHouseholdHeadById(oldHead.getId(), YesOrNoEnum.NO.getKey());
            }
        }else{
            if (oldHead!=null) {
                NotifLogsModel notifLogsModel = new NotifLogsModel(); //saving notif logs
                notifLogsModel.setRefNo(generateReferenceNumber(null));
                notifLogsModel.setUserId(oldHead.getUserId());
                notifLogsModel.setMessage("Hi, " + oldHead.getFirstNm() + "! Your household was set to " + SystemStatusEnum.getDscpByKey(modelObj.getStatus()) + ".");
                notifLogsModel.setRecipient(oldHead.getFullNm());
                notifLogsModel.setIsSmsEmail(oldHead.getEmailAddress()!=null ? ChannelEnum.ALL.getKey() : ChannelEnum.SMS.getKey());
                notifLogsModel.setSentDt(new Date());
                notifLogsModel.setType(LogsTypeEnum.HOUSEHOLD_UPDATE.getKey());
                notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
                notifLogsModel.setOtherDetail(LogsTypeEnum.HOUSEHOLD_UPDATE.getDesc() + " - " + SystemStatusEnum.getDscpByKey(modelObj.getStatus()));
                notifLogsModel.setMainActionStr(LogsTypeEnum.HOUSEHOLD_UPDATE.getMainAction());
                notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);
            }
        }

        if (!otherHouseholdList.isEmpty()) {
            Integer tempStatus = SystemStatusEnum.INACTIVE.getKey();
            for (HouseholdModel hh : otherHouseholdList) {
                HouseholdModel temp = new HouseholdModel();
                temp.setId(hh.getId());
                temp.setHouseholdDesc(hh.getHouseholdDesc());
                temp.setHouseholdUniqKey(hh.getHouseholdUniqKey());
                temp.setStatus(tempStatus);
                temp.setCreatedDt(hh.getCreatedDt());
                householdJDBCRepository.update(temp);

                List<UsersModel> members = usersJDBCRepository.findByHouseholdKeys(List.of(hh.getId()));
                if (members!=null && !members.isEmpty()) {
                    UsersModel head = members.stream().filter(u -> YesOrNoEnum.YES.getKey().equals(u.getIsHouseholdHead()))
                            .findFirst()
                            .orElse(null);

                    if (head!=null) {
                        NotifLogsModel notifLogsModel = new NotifLogsModel(); //saving notif logs
                        notifLogsModel.setRefNo(generateReferenceNumber(null));
                        notifLogsModel.setUserId(head.getUserId());
                        notifLogsModel.setMessage("Hi, " + head.getFirstNm() + "! Your household was set to " + SystemStatusEnum.getDscpByKey(tempStatus) + ".");
                        notifLogsModel.setRecipient(head.getFullNm());
                        notifLogsModel.setIsSmsEmail(head.getEmailAddress()!=null ? ChannelEnum.ALL.getKey() : ChannelEnum.SMS.getKey());
                        notifLogsModel.setSentDt(new Date());
                        notifLogsModel.setType(LogsTypeEnum.HOUSEHOLD_UPDATE.getKey());
                        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
                        notifLogsModel.setOtherDetail(LogsTypeEnum.HOUSEHOLD_UPDATE.getDesc() + " - " + SystemStatusEnum.getDscpByKey(tempStatus));
                        notifLogsModel.setMainActionStr(LogsTypeEnum.HOUSEHOLD_UPDATE.getMainAction());
                        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);

                        String msg = "Hi, " + head.getFirstNm() + "! Your household was set to " + SystemStatusEnum.getDscpByKey(tempStatus) + ".";

                        SmsModel sms = new SmsModel();
                        sms.setRecipient(head.formattedMobileNo());
                        sms.setMessage(msg);
                        smsService.sendSms(sms);

                        if (head.getEmailAddress()!=null) {
                            emailService.sendSimpleEmailNotif(head.getEmailAddress(), "Household Detail Update", msg);
                        }
                    }
                }
            }
        }

        /////////////// NOTIF AND SMS-EMAIL ///////////////

        if (oldHead!=null && oldHead.getId()!=null) {
            String msgForOldHead = "Hi, " + oldHead.getFirstNm() + "! You have been removed as the household head for " + modelObj.getHouseholdDesc() + ".";

            SmsModel oldHeadSms = new SmsModel();
            oldHeadSms.setRecipient(oldHead.formattedMobileNo());
            oldHeadSms.setMessage(msgForOldHead);
            smsService.sendSms(oldHeadSms);

            if (oldHead.getEmailAddress()!=null) {
                emailService.sendSimpleEmailNotif(oldHead.getEmailAddress(), "Household Detail Update", msgForOldHead);
            }
        }

        if (newHead.isPresent()) {
            String msgForNewHead = "Hi, " + newHead.get().getFirstNm() + "! You have been assigned as the household head for " + modelObj.getHouseholdDesc() + ".";

            SmsModel newHeadSms = new SmsModel();
            newHeadSms.setRecipient(newHead.get().formattedMobileNo());
            newHeadSms.setMessage(msgForNewHead);
            smsService.sendSms(newHeadSms);

            if (newHead.get().getEmailAddress()!=null) {
                emailService.sendSimpleEmailNotif(newHead.get().getEmailAddress(), "Household Detail Update", msgForNewHead);
            }

            NotifLogsModel notifLogsModel = new NotifLogsModel(); //saving notif logs
            notifLogsModel.setRefNo(generateReferenceNumber(null));
            notifLogsModel.setUserId(modelObj.getUserId());
            notifLogsModel.setMessage(newHeadSms.getMessage());
            notifLogsModel.setRecipient(newHead.get().getFullNm());
            notifLogsModel.setIsSmsEmail(newHead.get().getEmailAddress()!=null ? ChannelEnum.ALL.getKey() : ChannelEnum.SMS.getKey());
            notifLogsModel.setSentDt(new Date());
            notifLogsModel.setType(LogsTypeEnum.HOUSEHOLD_UPDATE.getKey());
            notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
            notifLogsModel.setOtherDetail(LogsTypeEnum.HOUSEHOLD_UPDATE.getDesc() + " - " + SystemStatusEnum.getDscpByKey(modelObj.getStatus()));
            notifLogsModel.setMainActionStr(LogsTypeEnum.HOUSEHOLD_UPDATE.getSecAction());
            notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);
        }

        //////////////////////////////////////////////////

        HouseholdReturnModel returnObj = new HouseholdReturnModel();
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.HOUSEHOLD
        ));

        return returnObj;
    }
}
