package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.AnnouncementJDBCRepository;
import com.javaguides.bms.jdbc.repository.LoginJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.AnnouncementReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl extends BaseServiceImpl implements AnnouncementService {

    static final String IS_REQUIRED_SUFFIX = " is required.";
    static final String ARE_REQUIRED_SUFFIX = " are required.";

    private final AnnouncementJDBCRepository announcementJDBCRepository;
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final LoginJDBCRepository loginJDBCRepository;

    private UsersJDBCRepository usersJDBCRepository;

    @Override
    public AnnouncementModel validateRequest(EnrollmentRequest requestObj) {
        AnnouncementModel announcement = new AnnouncementModel(requestObj);
        return validate(announcement);
    }

    public AnnouncementModel validate(AnnouncementModel modelObj) {
        List<String> errorList = new ArrayList<>();

        if (modelObj.getHeader()!=null) {
            modelObj.setHeader(modelObj.getHeader().trim());
        }else{
            errorList.add("Header" + IS_REQUIRED_SUFFIX);
        }

        if (modelObj.getType()==null) {
            errorList.add("Type" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setTypeString(SmsTypeEnum.getDescByKey(modelObj.getType()));
        }

        if (modelObj.getAlertStatus()==null) {
            errorList.add("Status" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setAlertTypeString(AlertStatusEnum.getDesc3ByKey(modelObj.getAlertStatus()));
        }

        if (modelObj.getIsSmsEmail()==null) {
            errorList.add("Channel" + IS_REQUIRED_SUFFIX);
        }else{
            modelObj.setChannelString(YesOrNoEnum.YES.getKey().equals(modelObj.getIsSmsEmail()) ? "SMS" : "Email");
        }

        if (modelObj.getMessage()==null || modelObj.getMessage().isEmpty()) {
            errorList.add("Message" + IS_REQUIRED_SUFFIX);
        }

        if (modelObj.getRecipientKeys()!=null && !modelObj.getRecipientKeys().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer val : modelObj.getRecipientKeys()) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(ResidentClassificationEnum.getDescByKey(val));
            }
            modelObj.setRecipientTypeString(sb.toString());

            if (modelObj.getRecipientKeys().contains(ResidentClassificationEnum.ALL.getKey())) {
                modelObj.setRecipientKeys(ResidentClassificationEnum.getAllKeys());
            }
            createListOfAnnouncementObjPerRecipient(modelObj);
        }else{
            errorList.add("Recipients" + ARE_REQUIRED_SUFFIX);
        }

        if (!errorList.isEmpty()) throwErrorMessages(errorList);
        return modelObj;
    }


    public void createListOfAnnouncementObjPerRecipient(AnnouncementModel modelObj) {
        List<UsersModel> users = usersJDBCRepository.findAllUsersByClassificationKeys(modelObj.getRecipientKeys());
        if (users == null || users.isEmpty()) {
            modelObj.setAnnouncementModels(Collections.emptyList());
            throwErrorMessage("No recipient found.");
            return;
        }

        String recipientNames = users.stream()
                .map(UsersModel::getFullNm2)
                .collect(Collectors.joining(", "));
        modelObj.setRecipientListString(recipientNames);

        Date createdDt = new Date();
        String generatedGrpId = UUID.randomUUID().toString().replace("-", "");
        List<AnnouncementModel> models = users.stream()
                .map(u -> {
                    AnnouncementModel m = new AnnouncementModel();
                    m.setUserId(u.getId());
                    m.setRefNo(generateReferenceNumber(ServicesEnum.ADD_ANNOUNCEMENT.getCode()));
                    m.setGrpId(generatedGrpId);
                    m.setRecipientNm(u.getFullNm());
                    m.setHeader(modelObj.getHeader());
                    m.setIsSmsEmail(modelObj.getIsSmsEmail());
                    m.setType(modelObj.getType());
                    m.setAlertStatus(modelObj.getAlertStatus());
                    m.setMessage(modelObj.getMessage());
                    m.setCreatedDt(createdDt);
                    return m;
                })
                .collect(Collectors.toList());

        modelObj.setAnnouncementModels(models);
    }

    @Override
    public AnnouncementReturnModel saveRequest(EnrollmentRequest request) {
        AnnouncementModel model = new AnnouncementModel(request);
        validate(model);
        createListOfAnnouncementObjPerRecipient(model);
        AnnouncementReturnModel returnModel = new AnnouncementReturnModel(model);
        List<NotifLogsModel> notifList = new ArrayList<>();

        if (returnModel.getAnnouncementModels()!=null && !returnModel.getAnnouncementModels().isEmpty()) {
            returnModel.getAnnouncementModels().forEach(modelObj -> {
                NotifLogsModel notifLogsModel = new NotifLogsModel();
                notifLogsModel.setRefNo(generateReferenceNumber(null));
                notifLogsModel.setUserId(modelObj.getId());
                notifLogsModel.setMessage(modelObj.getMessage());
                notifLogsModel.setRecipient(modelObj.getRecipientNm());
                notifLogsModel.setIsSmsEmail(modelObj.getIsSmsEmail());
                notifLogsModel.setSentDt(new Date());
                notifLogsModel.setType(modelObj.getType());
                notifLogsModel.setStatus(modelObj.getStatus());
                notifList.add(notifLogsModel);
            });
        }

        announcementJDBCRepository.saveBatch(returnModel.getAnnouncementModels());
        notifLogsJDBCRepository.saveBatch(notifList);

        assert returnModel.getAnnouncementModels()!=null;
        boolean isSingle = returnModel.getAnnouncementModels().size() == 1;
        returnModel.setAckMessage(StringMessagesUtil.formatMsgString(
                isSingle ? StringMessagesUtil.SENT_SINGLE_SUFFIX : StringMessagesUtil.SENT_MULTIPLE_SUFFIX,
                isSingle ? StringMessagesUtil.ANNOUNCEMENT : StringMessagesUtil.ANNOUNCEMENTS
        ));
        return returnModel;
    }

    @Override
    public Page<AnnouncementReturnModel> searchAnnouncement(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<AnnouncementModel> notifLogs = announcementJDBCRepository.searchAnnouncement(searchRequest, pageRequest);
        return notifLogs.map(AnnouncementReturnModel::new);
    }

    @Override
    public Map<String, List<AnnouncementModel>> getAnnouncementListGrouped(Integer roleKey, HttpSession session) {
        Map<String, List<AnnouncementModel>> grouped = new HashMap<>();
        if (SystemUserEnum.SYSTEM_USER.getKey().equals(roleKey)) {
            Object userObj = session.getAttribute("user");
            if (userObj!=null) {
                LoginCreds user = (LoginCreds) userObj;
                List<LoginCreds> list = loginJDBCRepository.getUserById(user.getUserId());
                if (list==null || list.isEmpty()) {
                    return null;
                } else if (list.size()>1) {
                    return null;
                } else {
                    LoginCreds loginCreds = list.get(0);
                    List<AnnouncementModel> modelObj = announcementJDBCRepository.findAnnouncementByUserIdGrouped(loginCreds.getUserId());
                    grouped = modelObj.stream().collect(Collectors.groupingBy(a ->
                            DateUtil.getDateStringWithFormat(a.getCreatedDt(), DateFormatEnum.DT_FORMAT_5.getPattern()), LinkedHashMap::new, Collectors.toList()));
                }
            }
        }
        return grouped;
    }
}
