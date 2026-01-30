package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.helper.WordPreview;
import com.javaguides.bms.jdbc.repository.DocumentJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.DocumentModel;
import com.javaguides.bms.model.LoginCreds;
import com.javaguides.bms.model.NotifLogsModel;
import com.javaguides.bms.model.UsersModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.DocumentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.DocumentReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import jakarta.servlet.http.HttpSession;
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

    @Override
    public DocumentReturnModel validateRequest(DocumentRequest documentRequest, HttpSession session) {
        DocumentModel modelObj = new DocumentModel(documentRequest);
        return validateObj(modelObj, session);
    }

    public DocumentReturnModel validateObj(DocumentModel model, HttpSession session) {
        List<String> errorList = new ArrayList<>();

        if (model.getDocumentType()!=null) {
            model.setDocumentTypeString(DocumentTypeEnum.getDescByKey(model.getDocumentType()));
        }else{
            errorList.add("Document Type" + IS_REQUIRED_SUFFIX);
        }

        if (model.getPurpose()!=null) {
            model.setPurpose(model.getPurpose().trim().toUpperCase());
        }else{
            errorList.add("Purpose" + IS_REQUIRED_SUFFIX);
        }

        if (model.getStatus()==null) {
            model.setStatus(SystemStatusEnum.PENDING.getKey());
        }

        if (model.getDateRequested()==null) {
            model.setDateRequested(new Date());
        }

        if (model.getUserId()==null) {
            Object user = session.getAttribute("user");
            if (user instanceof LoginCreds currentUser) {
                model.setUserId(currentUser.getUserId());
            }
        }

        List<DocumentModel> list = documentJDBCRepository.findPendingRequestByUserIdAndKey(model.getUserId(), model.getDocumentType());
        boolean hasExistingRequest = list.stream().anyMatch(doc -> SystemStatusEnum.PENDING.getKey().equals(doc.getStatus()));
        if (hasExistingRequest) errorList.add("You still have a pending " + DocumentTypeEnum.getDescByKey(model.getDocumentType()) + " request.");

        if (!errorList.isEmpty()) throwErrorMessages(errorList);

        return new DocumentReturnModel(model);
    }

    @Override
    public DocumentReturnModel saveRequest(DocumentRequest documentRequest) {
        DocumentModel modelObj = new DocumentModel(documentRequest);
        validateObj(modelObj, null);

        Optional<UsersModel> usersModel = usersJDBCRepository.findById(modelObj.getUserId());
        UsersModel user = usersModel.orElseGet(UsersModel::new);

        if (user.getMobileNo() != null && user.getMobileNo().startsWith("0")) {
            user.setFormattedMobileNo("+63" + user.getMobileNo().substring(1));
        }

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("${BARANGAY_NAME}", "Paliparan III");
        placeholders.put("${CITY}", "Dasmariñas City, Cavite");
        placeholders.put("${RESIDENT_NAME}", "<strong>" + user.getFullNm() + "</strong>");
        placeholders.put("${RESIDENT_ADDRESS}", "<strong>" + user.getHomeAddress() + "</strong>");
        placeholders.put("${RESIDENCY_DATE}", "<strong>" + DateUtil.getDateStringWithFormat(user.getDateEnrolled(), DateFormatEnum.DT_FORMAT_1.getPattern()) + "</strong>");
        placeholders.put("${PURPOSE}", "<strong>" + modelObj.getPurpose() + "</strong>");
        placeholders.put("${DATE}", "<strong>" + DateUtil.getDateStringWithFormat(documentRequest.getDateRequested(), DateFormatEnum.DT_FORMAT_5.getPattern()) + "</strong>");


        DocumentTypeEnum docType = DocumentTypeEnum.getByKey(modelObj.getDocumentType());
        if (docType != null) {
            modelObj.setHeader(replacePlaceholders(docType.getHeaderTemplate(), placeholders));
            modelObj.setBody(replacePlaceholders(docType.getBodyTemplate(), placeholders));
            modelObj.setFooter(replacePlaceholders(docType.getFooterTemplate(), placeholders));
        }

        SmsModel sms = new SmsModel();
        sms.setRecipient(user.getFormattedMobileNo());
        sms.setMessage("Hi, " + user.getFirstNm() + "! Your " + modelObj.getDocumentTypeString() + " has been submitted successfully.");
        //smsService.sendSms(sms);

        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getUserId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(user.getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(SmsTypeEnum.DOCUMENT_REQUEST.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
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
    public String previewRequest(DocumentRequest requestObj, HttpSession session) {
        LoginCreds user = (LoginCreds) session.getAttribute("user");
        UsersModel userObj = usersJDBCRepository.findById(user.getUserId()).orElse(new UsersModel());

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
        Page<DocumentModel> users = documentJDBCRepository.searchRequests(searchRequest, pageRequest);
        return users.map(DocumentReturnModel::new);
    }

    @Override
    public DocumentReturnModel getRequestById(String id) {
        DocumentReturnModel returnObj = new DocumentReturnModel();
        Optional<DocumentModel> document = documentJDBCRepository.findById(id);
        if (document.isPresent()) {
            Optional<UsersModel> user = usersJDBCRepository.findById(document.get().getUserId());

            returnObj.setId(document.get().getId());
            returnObj.setUserId(document.get().getUserId());
            returnObj.setPurpose(document.get().getPurpose());
            returnObj.setDocumentType(document.get().getDocumentType());
            returnObj.setDocumentTypeString(DocumentTypeEnum.getDescByKey(returnObj.getDocumentType()));
            returnObj.setStatus(document.get().getStatus());
            returnObj.setStatusString(SystemStatusEnum.getDscpByKey(returnObj.getStatus()));
            returnObj.setRefNo(document.get().getRefNo());
            returnObj.setDateRequested(document.get().getDateRequested());
            returnObj.setDateRequestedString(DateUtil.getDateStringWithFormat(returnObj.getDateRequested(), DateFormatEnum.DT_FORMAT_12.getPattern()));
            returnObj.setRequestor(user.isPresent() ? user.get().getFullNm() : "");
            returnObj.setHeader(document.get().getHeader());
            String body = document.get().getBody();

            body = body.replaceAll("\\d{2}/\\d{2}/\\d{4}",
                    DateUtil.getDateStringWithFormat(new Date(), DateFormatEnum.DT_FORMAT_5.getPattern())
            );
            returnObj.setBody(body);

            returnObj.setFooter(document.get().getFooter());
        }
        return returnObj;
    }

    @Override
    public DocumentReturnModel processDocument(DocumentRequest request) {
        DocumentModel document = new DocumentModel(request);
        document.setDateProcessed(new Date());
        documentJDBCRepository.updateDocument(document);

        return new DocumentReturnModel(document);
    }

}
