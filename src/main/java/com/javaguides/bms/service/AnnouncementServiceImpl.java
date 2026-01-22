package com.javaguides.bms.service;

import com.javaguides.bms.enums.AlertStatusEnum;
import com.javaguides.bms.enums.ResidentClassificationEnum;
import com.javaguides.bms.enums.SmsTypeEnum;
import com.javaguides.bms.enums.YesOrNoEnum;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.AnnouncementJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.AnnouncementModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.returnmodel.AnnouncementReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnnouncementServiceImpl extends BaseServiceImpl implements AnnouncementService {

    static final String IS_REQUIRED_SUFFIX = " is required.";
    static final String ARE_REQUIRED_SUFFIX = " are required.";

    private final AnnouncementJDBCRepository announcementJDBCRepository;

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
//            modelObj.setAnnouncementModels(Collections.emptyList());
            return;
        }

        String recipientNames = users.stream()
                .map(UsersModel::getFullNm2)
                .collect(Collectors.joining(", "));
        modelObj.setRecipientListString(recipientNames);

        Date createdDt = new Date();
        List<AnnouncementModel> models = users.stream()
                .map(u -> {
                    AnnouncementModel m = new AnnouncementModel();
                    m.setUserId(u.getId());
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

//        modelObj.setAnnouncementModels(models);
    }

    @Override
    public AnnouncementReturnModel saveRequest(EnrollmentRequest request) {
        AnnouncementModel model = new AnnouncementModel(request);
        validate(model);
        createListOfAnnouncementObjPerRecipient(model);
        AnnouncementReturnModel returnModel = new AnnouncementReturnModel(model);

        if (returnModel.getAnnouncementModels()!=null && !returnModel.getAnnouncementModels().isEmpty()) {
            returnModel.getAnnouncementModels().forEach(announcementJDBCRepository::saveRequest);
        }

        assert returnModel.getAnnouncementModels()!=null;
        boolean isSingle = returnModel.getAnnouncementModels().size() == 1;
        returnModel.setAckMessage(StringMessagesUtil.formatMsgString(
                isSingle ? StringMessagesUtil.SENT_SINGLE_SUFFIX : StringMessagesUtil.SENT_MULTIPLE_SUFFIX,
                isSingle ? StringMessagesUtil.ANNOUNCEMENT : StringMessagesUtil.ANNOUNCEMENTS
        ));
        returnModel.setRefNo(generateReferenceNumber(null));
        return returnModel;
    }

}
