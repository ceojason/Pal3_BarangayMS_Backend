package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.NumberFormatterUtil;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.helper.WordPreview;
import com.javaguides.bms.jdbc.repository.DocumentJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.DocumentModel;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.DocumentReturnModel;
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
public class DocumentServiceImpl extends BaseServiceImpl implements DocumentService {

    static final String IS_REQUIRED_SUFFIX = " is required.";
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final UsersJDBCRepository usersJDBCRepository;
    private final DocumentJDBCRepository documentJDBCRepository;
    private final EmailService emailService;
    private final ConfigService configService;
    private final SmsService smsService;

    @Override
    public DocumentReturnModel validateRequest(DocumentRequest documentRequest, String userId) {
        DocumentModel modelObj = new DocumentModel(documentRequest);
        return validateObj(modelObj, userId);
    }

    public DocumentReturnModel validateObj(DocumentModel model, String userId) {
        List<String> errorList = new ArrayList<>();

        if (model.getDocuCategoryKey()!=null) {
            model.setDocuCategoryKeyString(DocumentCategoryEnum.getDocuCatDescByKey(model.getDocuCategoryKey()));
        }else{
            errorList.add("Document Category" + IS_REQUIRED_SUFFIX);
        }

        if (model.getDocuSubCategoryKey()!=null) {
            model.setDocuSubCategoryKeyString(DocumentSubCatEnum.getDocuSubCatDescByKey(model.getDocuSubCategoryKey()));
        }else{
            errorList.add("Document File" + IS_REQUIRED_SUFFIX);
        }

        model.setProcessFeeString(NumberFormatterUtil.format(model.getProcessFee()));

        if (model.getPurposeKey()!=null) {
            model.setPurposeKeyString(DocumentPurposeEnum.getDocuPurposeDescByKey(model.getPurposeKey()));
        }else{
            errorList.add("Purpose" + IS_REQUIRED_SUFFIX);
        }

        if (DocumentPurposeEnum.OTHERS.getKey().equals(model.getPurposeKey())) {
            if (model.getOthPurpose() == null || model.getOthPurpose().trim().isEmpty()) {
                errorList.add("Please specify the purpose of requesting the document.");
            } else {
                model.setOthPurpose(model.getOthPurpose().trim().toUpperCase());
            }
        }else{
            model.setOthPurpose(null);
        }

        if (model.getStatus()==null) {
            model.setStatus(SystemStatusEnum.PENDING.getKey());
        }

        if (model.getDateRequested()==null) {
            model.setDateRequested(new Date());
        }

        if (userId!=null) {
            model.setUserId(userId);
            Optional<UsersModel> user = usersJDBCRepository.findById(userId);
            if (user.isPresent()) {
                String configAddressPrefix = configService.getAddressConfigObj();
                model.setAddressFromConfig(configAddressPrefix);
                model.setRequestor(user.get().getFullNm());
                if (user.get().getBirthDt()!=null) {
                    model.setBirthDt(user.get().getBirthDt());
                    model.setBirthDtString(DateUtil.getDateStringWithFormat(user.get().getBirthDt(), DateFormatEnum.DT_FORMAT_1.getPattern()));
                }
                if (user.get().getGender()!=null) {
                    model.setGender(user.get().getGender());
                    model.setGenderString(GenderEnum.getGenderDscpFromKeyStr(user.get().getGender()));
                }
                if (user.get().getCivilStatusKey()!=null) {
                    model.setCivilStatusKey(user.get().getCivilStatusKey());
                    model.setCivilStatusString(CivilStatusEnum.getCivilStatusDescByKey(user.get().getCivilStatusKey()));
                }
                if (user.get().getMobileNo()!=null) {
                    model.setMobileNo(user.get().getMobileNo());
                }
                if (user.get().getBlock()!=null) {
                    model.setBlock(user.get().getBlock());
                }
                if (user.get().getLot()!=null) {
                    model.setLot(user.get().getLot());
                }
                if (user.get().getStreet()!=null) {
                    model.setStreet(user.get().getStreet());
                }
                if (user.get().getPhaseKey()!=null) {
                    model.setPhaseKey(user.get().getPhaseKey());
                }
            }else{
                errorList.add("Document request cannot proceed at this time.");
            }
        }

        List<DocumentModel> list = documentJDBCRepository.findPendingRequestByUserIdAndKeys(model.getUserId(), model.getDocuCategoryKey(), model.getDocuSubCategoryKey());
        boolean hasExistingRequest = list.stream().anyMatch(doc -> SystemStatusEnum.PENDING.getKey().equals(doc.getStatus()));
        if (hasExistingRequest) errorList.add("You still have a pending " + DocumentCategoryEnum.getDocuCatDescByKey(model.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(model.getDocuSubCategoryKey())
                + " request.");

        if (!errorList.isEmpty()) throwErrorMessages(errorList);

        return new DocumentReturnModel(model);
    }

    @Override
    public DocumentReturnModel saveRequest(DocumentRequest documentRequest) {
        DocumentModel modelObj = new DocumentModel(documentRequest);
        validateObj(modelObj, null);

        SmsModel sms = new SmsModel();

        Optional<UsersModel> user = usersJDBCRepository.findById(documentRequest.getUserId());
        if (user.isPresent()) {
            String configAddressPrefix = configService.getAddressConfigObj();
            modelObj.setAddressFromConfig(configAddressPrefix);
            modelObj.setRequestor(user.get().getFullNm());
            if (user.get().getBirthDt()!=null) {
                modelObj.setBirthDt(user.get().getBirthDt());
                modelObj.setBirthDtString(DateUtil.getDateStringWithFormat(user.get().getBirthDt(), DateFormatEnum.DT_FORMAT_1.getPattern()));
            }
            if (user.get().getGender()!=null) {
                modelObj.setGender(user.get().getGender());
                modelObj.setGenderString(GenderEnum.getGenderDscpFromKeyStr(user.get().getGender()));
            }
            if (user.get().getCivilStatusKey()!=null) {
                modelObj.setCivilStatusKey(user.get().getCivilStatusKey());
                modelObj.setCivilStatusString(CivilStatusEnum.getCivilStatusDescByKey(user.get().getCivilStatusKey()));
            }
            if (user.get().getMobileNo()!=null) {
                modelObj.setMobileNo(user.get().getMobileNo());
            }
            if (user.get().getBlock()!=null) {
                modelObj.setBlock(user.get().getBlock());
            }
            if (user.get().getLot()!=null) {
                modelObj.setLot(user.get().getLot());
            }
            if (user.get().getStreet()!=null) {
                modelObj.setStreet(user.get().getStreet());
            }
            if (user.get().getPhaseKey()!=null) {
                modelObj.setPhaseKey(user.get().getPhaseKey());
            }

//            if (user.get().getMobileNo() != null && user.get().getMobileNo().startsWith("0")) {
//                user.get().setFormattedMobileNo("+63" + user.get().getMobileNo().substring(1));
//            }
            sms.setRecipient(user.get().formattedMobileNo());
            sms.setMessage("Hi, " + user.get().getFirstNm() + "! Your " + DocumentCategoryEnum.getDocuCatDescByKey(modelObj.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(modelObj.getDocuSubCategoryKey()) + " request has been submitted successfully.");

            if (user.get().getEmailAddress()!=null) {
                emailService.sendSimpleEmailNotif(user.get().getEmailAddress(), LogsTypeEnum.DOCUMENT_REQUEST.getMainAction(), sms.getMessage());
            }
            smsService.sendSms(sms);
        }else{
            throwErrorMessage("Document request cannot proceed at this time.");
        }

        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getUserId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(user.get().getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(LogsTypeEnum.DOCUMENT_REQUEST.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsModel.setOtherDetail(DocumentCategoryEnum.getDocuCatDescByKey(modelObj.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(modelObj.getDocuSubCategoryKey()));
        notifLogsModel.setMainActionStr(LogsTypeEnum.DOCUMENT_REQUEST.getMainAction());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);

        modelObj.setRefNo(notifLogsModel.getRefNo());
        documentJDBCRepository.saveRequest(modelObj);

        DocumentReturnModel returnObj = new DocumentReturnModel(modelObj);
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.SUBMITTED_SINGLE_SUFFIX,
                "Document Request"
        ));

        return returnObj;
    }

    private String replacePlaceholders(String template, Map<String, String> placeholders) {
        if (template == null || template.isEmpty()) return "";
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            template = template.replace(entry.getKey(), entry.getValue());
        }
        return template;
    }


    @Override
    public String previewRequest(DocumentRequest requestObj, String userId) {
        UsersModel userObj = usersJDBCRepository.findById(userId).orElse(new UsersModel());

        Map<String, String> props = Map.of(
                "${RESIDENT_NAME}", userObj.getFullNm(),
                "${RESIDENT_ADDRESS}", userObj.getHomeAddress(),
                "${RESIDENCY_DATE}", DateUtil.getDateStringWithFormat(userObj.getDateEnrolled(), DateFormatEnum.DT_FORMAT_5.getPattern()),
                "${PURPOSE}", requestObj.getPurpose(),
                "${DATE}", DateUtil.getDateStringWithFormat(new Date(), DateFormatEnum.DT_FORMAT_5.getPattern()),
                "${BARANGAY_NAME}", "Paliparan III",
                "${CITY}", "Dasmariñas City, Cavite"
        );

        String templatePath = DocumentTypeEnum.getTemplateByKey(requestObj.getDocumentType());
        if (templatePath == null) {
            return "<p style='color:red'>No template found for this document type</p>";
        }

        try {
            return WordPreview.generatePreview(templatePath, props);
        } catch (Exception e) {
            e.printStackTrace();
            return "<p style='color:red'>Failed to load document template: " + e.getMessage() + "</p>";
        }
    }

    @Override
    public Page<DocumentReturnModel> searchRequests(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<DocumentModel> documents = documentJDBCRepository.searchRequests(searchRequest, pageRequest);
        String configAddressPrefix = configService.getAddressConfigObj();
        documents.stream().forEach(item -> {
            item.setAddressFromConfig(configAddressPrefix);
        });
        return documents.map(DocumentReturnModel::new);
    }

    @Override
    public DocumentReturnModel getRequestById(String id) {
        DocumentReturnModel returnObj = new DocumentReturnModel();
        Optional<DocumentModel> document = documentJDBCRepository.findById(id);
        if (document.isPresent()) {
            Optional<UsersModel> user = usersJDBCRepository.findById(document.get().getUserId());

            returnObj.setId(document.get().getId());
            returnObj.setUserId(document.get().getUserId());

            returnObj.setDocuCategoryKey(document.get().getDocuCategoryKey());
            returnObj.setDocuSubCategoryKey(document.get().getDocuSubCategoryKey());
            returnObj.setProcessFee(document.get().getProcessFee());
            returnObj.setPurposeKey(document.get().getPurposeKey());
            returnObj.setOthPurpose(document.get().getOthPurpose());
            returnObj.setDocuCategoryKeyString(DocumentCategoryEnum.getDocuCatDescByKey(document.get().getDocuCategoryKey()));
            returnObj.setDocuSubCategoryKeyString(DocumentSubCatEnum.getDocuSubCatDescByKey(document.get().getDocuSubCategoryKey()));
            returnObj.setPurposeKeyString(DocumentPurposeEnum.getDocuPurposeDescByKey(document.get().getPurposeKey()));
            returnObj.setProcessFeeString(NumberFormatterUtil.format(document.get().getProcessFee()));

            returnObj.setStatus(document.get().getStatus());
            returnObj.setStatusString(SystemStatusEnum.getDscpByKey(returnObj.getStatus()));
            returnObj.setRefNo(document.get().getRefNo());
            returnObj.setDateRequested(document.get().getDateRequested());
            returnObj.setDateRequestedString(DateUtil.getDateStringWithFormat(returnObj.getDateRequested(), DateFormatEnum.DT_FORMAT_12.getPattern()));
            returnObj.setRequestor(user.isPresent() ? user.get().getFullNm() : "");
        }
        return returnObj;
    }

    @Override
    public DocumentReturnModel processDocument(DocumentRequest request) {
        DocumentModel document = new DocumentModel(request);
        document.setDateProcessed(new Date());
        documentJDBCRepository.updateDocument(document);

        Optional<UsersModel> user = usersJDBCRepository.findById(request.getUserId());

        if (user.isPresent()) {
            String message;
            String mainAction;
            if (document.getStatus().equals(SystemStatusEnum.PROCESSED.getKey())) {
                mainAction = "Processed";
                message = "Hi, " + user.get().getFirstNm() + "! Your " + DocumentCategoryEnum.getDocuCatDescByKey(document.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(document.getDocuSubCategoryKey()) + " request with reference number " + document.getRefNo()
                        + " has been processed and can now be claimed at barangay office. Please ready a total amount of ₱"+ NumberFormatterUtil.format(document.getProcessFee()) + " for the processing fee. Thank you!";
            }else{
                mainAction = "Rejected";
                message = "Hi, " + user.get().getFirstNm() + "! Your " + DocumentCategoryEnum.getDocuCatDescByKey(document.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(document.getDocuSubCategoryKey()) + " request with reference number " + document.getRefNo()
                        + " has been rejected and cannot be processed at the moment. You can follow-up with your request with the assigned barangay personnel. Thank you!";
            }

            if (user.get().getEmailAddress()!=null) {
                emailService.sendSimpleEmailNotif(user.get().getEmailAddress(), "Document Request" + " - " + mainAction, message);
            }

            SmsModel sms = new SmsModel();
            sms.setRecipient(user.get().formattedMobileNo());
            sms.setMessage(message);
            smsService.sendSms(sms);

            NotifLogsModel notifLogsModel = new NotifLogsModel();
            notifLogsModel.setRefNo(generateReferenceNumber(null));
            notifLogsModel.setUserId(user.get().getId());
            notifLogsModel.setMessage(sms.getMessage());
            notifLogsModel.setRecipient(user.get().getFullNm());
            notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
            notifLogsModel.setSentDt(new Date());
            notifLogsModel.setType(LogsTypeEnum.DOCUMENT_REQUEST.getKey());
            notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
            notifLogsModel.setOtherDetail((DocumentCategoryEnum.getDocuCatDescByKey(document.getDocuCategoryKey()) + " - " + DocumentSubCatEnum.getDocuSubCatDescByKey(document.getDocuSubCategoryKey())));
            notifLogsModel.setMainActionStr(LogsTypeEnum.DOCUMENT_REQUEST.getDesc()+ " - " + mainAction);
            notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);
        }else{
            throwErrorMessage("An error occurred upon processing.");
        }

        return new DocumentReturnModel(document);
    }

}
