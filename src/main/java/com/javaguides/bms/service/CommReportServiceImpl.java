package com.javaguides.bms.service;

import com.javaguides.bms.enums.*;
import com.javaguides.bms.helper.KeyValueModelStr;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.CommReportJDBCRepository;
import com.javaguides.bms.jdbc.repository.DocumentJDBCRepository;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.UsersJDBCRepository;
import com.javaguides.bms.model.*;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.basemodel.SmsModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import com.javaguides.bms.model.requestmodel.searchrequest.MainSearchRequest;
import com.javaguides.bms.model.returnmodel.CommReportReturn;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import com.javaguides.bms.service.baseservice.EmailService;
import com.javaguides.bms.service.baseservice.SmsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@AllArgsConstructor
public class CommReportServiceImpl extends BaseServiceImpl implements CommReportService {

    static final String IS_REQUIRED_SUFFIX = " is required.";

    private final UsersJDBCRepository usersJDBCRepository;
    private final DocumentJDBCRepository documentJDBCRepository;
    private final EmailService emailService;
    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final CommReportJDBCRepository commReportJDBCRepository;
    private final SmsService smsService;

    private static final String BASE_UPLOAD_DIR =
            "C:/Dev Works/Pal3_BarangayMS_Backend/uploads/comm-reports-media";

    private static final String TEMP_UPLOAD_DIR =
            BASE_UPLOAD_DIR + "/temp";

    private static final String FINAL_UPLOAD_DIR =
            BASE_UPLOAD_DIR + "/final";

    @Override
    public CommReportReturn validateRequest(EnrollmentRequest requestObj, String userId) throws IOException {
        CommReportModel modelObj = new CommReportModel(requestObj);
        return validateObj(modelObj, userId);
    }

    public CommReportReturn validateObj(CommReportModel modelObj, String userId) throws IOException {
        modelObj.setErrorList(new ArrayList<>());
        if (modelObj.getReportTypeKey()==null) {
            modelObj.getErrorList().add("Report Type" + IS_REQUIRED_SUFFIX);
        }else{
            if (modelObj.getReportTypeKey().equals(CommReportTypeEnum.OTHER.getKey())) {
                if (modelObj.getOthTitle()==null || modelObj.getOthTitle().isEmpty()) {
                    modelObj.getErrorList().add("Custom Report Header" + IS_REQUIRED_SUFFIX);
                }else{
                    modelObj.setOthTitle(modelObj.getOthTitle().trim().toUpperCase());
                }
            }
        }

        if (modelObj.getHappensNearOrInHousehold()==null) {
            modelObj.getErrorList().add("Happens in your household or near household field" + IS_REQUIRED_SUFFIX);
        }

        if (modelObj.getHappensNearOrInHousehold()!=null &&
            modelObj.getHappensNearOrInHousehold().equals(YesOrNoEnum.YES.getKey())) {
            if (userId!=null) {
                Optional<UsersModel> userFetch = usersJDBCRepository.findById(userId);
                if (userFetch.isPresent()) {modelObj.setLocation(userFetch.get().getHomeAddress());
                }else{
                    modelObj.getErrorList().add("Session timeout. Please refresh the page.");
                }
            }else{
                modelObj.getErrorList().add("Session timeout. Please refresh the page.");
            }
        }

        if (modelObj.getHappensNearOrInHousehold()!=null && YesOrNoEnum.NO.getKey().equals(modelObj.getHappensNearOrInHousehold())) {
            if (modelObj.getLocation()!=null && !modelObj.getLocation().isEmpty()) {
                modelObj.setLocation(modelObj.getLocation().trim().toUpperCase());
            }else{
                modelObj.getErrorList().add("Please specify the exact location of the report.");
            }
        }

        if (modelObj.getDescription()!=null && !modelObj.getDescription().isEmpty()) {
            modelObj.setDescription(modelObj.getDescription().trim().toUpperCase());
        }else{
            modelObj.getErrorList().add("Description" + IS_REQUIRED_SUFFIX);
        }

        if (modelObj.getErrorList() != null && !modelObj.getErrorList().isEmpty()) throwErrorMessages(modelObj.getErrorList());

        // =========================
        // TEMP FILE UPLOAD SECTION
        // =========================

        List<String> urls = new ArrayList<>();
        if (modelObj.getAttachments() != null) {
            Path tempDirectoryPath = Paths.get(TEMP_UPLOAD_DIR);
            Files.createDirectories(tempDirectoryPath);

            for (MultipartFile file : modelObj.getAttachments()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = tempDirectoryPath.resolve(fileName);

                Files.write(filePath, file.getBytes());
                urls.add("/uploads/comm-reports-media/temp/" + fileName);
            }
            modelObj.setAttachmentUrls(urls);
        }
        return new CommReportReturn(modelObj);
    }

    // ====================================
    // FINALIZE FILES AFTER ACTUAL SAVE
    // ====================================

    public List<String> moveTempFilesToFinal(List<String> tempUrls, String reportId) throws IOException {

        List<String> finalUrls = new ArrayList<>();
        if (tempUrls==null || tempUrls.isEmpty()) return finalUrls;

        Path finalDirectory = Paths.get(FINAL_UPLOAD_DIR + "/" + reportId);
        Files.createDirectories(finalDirectory);

        for (String tempUrl : tempUrls) {
            String fileName = tempUrl.substring(tempUrl.lastIndexOf("/") + 1);
            Path tempFile = Paths.get(TEMP_UPLOAD_DIR).resolve(fileName);
            Path finalFile = finalDirectory.resolve(fileName);

            if (Files.exists(tempFile)) {
                Files.copy(tempFile, finalFile, StandardCopyOption.REPLACE_EXISTING);
                finalUrls.add("/uploads/comm-reports-media/final/" + reportId + "/" + fileName);
            }
        }
        return finalUrls;
    }

    @Override
    public CommReportReturn saveRequest(EnrollmentRequest requestObj, String userId) throws IOException {

        CommReportModel modelObj = new CommReportModel(requestObj);
        validateObj(modelObj, userId);

        Optional<UsersModel> user = usersJDBCRepository.findById(userId);
        if (user.isEmpty()) {
            throwErrorMessage("Filing of Community Report cannot proceed at this time.");
        }

        String refNo = generateReferenceNumber(null);
        modelObj.setUserId(userId);
        modelObj.setStatus(SystemStatusEnum.PENDING.getKey());
        modelObj.setRemarks(null);
        modelObj.setAssigneeId(null);
        modelObj.setPriority(PriorityEnum.NORMAL.getKey());
        modelObj.setRefNo(refNo);
        String reportId = commReportJDBCRepository.save(modelObj);
        List<String> finalUrls = moveTempFilesToFinal(modelObj.getAttachmentUrls(), reportId);

        // =========================
        // SAVE ATTACHMENTS TO DB
        // =========================
        if (finalUrls != null && !finalUrls.isEmpty()) {
            List<CommReportAttachmentModel> attachments = new ArrayList<>();
            for (String url : finalUrls) {
                String fileName = url.substring(url.lastIndexOf("/") + 1);

                CommReportAttachmentModel attach = new CommReportAttachmentModel();
                attach.setReportId(reportId);
                attach.setFileName(fileName);
                attach.setFilePath(url);
                attach.setFileType(null);
                attach.setFileSize(null);
                attach.setIsPrimary(false);
                attachments.add(attach);
            }
            commReportJDBCRepository.saveAttachments(attachments);
        }

        // =========================
        // SMS / EMAIL LOGIC
        // =========================
        SmsModel sms = new SmsModel();
        //if (user.get().getMobileNo() != null && user.get().getMobileNo().startsWith("0")) {
        //    user.get().setFormattedMobileNo("+63" + user.get().getMobileNo().substring(1));
        //}

        String otherDetailString = modelObj.getReportTypeKey()!=null && !modelObj.getReportTypeKey().equals(CommReportTypeEnum.OTHER.getKey())
                ? CommReportTypeEnum.getDescBKey(modelObj.getReportTypeKey()) : modelObj.getOthTitle();

        sms.setRecipient(user.get().formattedMobileNo());
        sms.setMessage("Hi, " + user.get().getFirstNm() + "! Your Community Report " + otherDetailString + " with Ref. No: " + refNo + " has been submitted successfully.");
        smsService.sendSms(sms);
        if (user.get().getEmailAddress() != null) {
            emailService.sendSimpleEmailNotif(user.get().getEmailAddress(), "Community Report: " + otherDetailString, sms.getMessage());
        }

        NotifLogsModel notifLogsModel = new NotifLogsModel();
        notifLogsModel.setRefNo(refNo);
        notifLogsModel.setUserId(modelObj.getUserId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(user.get().getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(LogsTypeEnum.COMM_REPORT.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsModel.setOtherDetail("Community Report: " + otherDetailString);
        notifLogsModel.setMainActionStr(LogsTypeEnum.COMM_REPORT.getMainAction());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);

        modelObj.setRefNo(notifLogsModel.getRefNo());
        CommReportReturn returnObj = new CommReportReturn(modelObj);
        returnObj.setAckMessage(
                StringMessagesUtil.formatMsgString(
                        StringMessagesUtil.SUBMITTED_SINGLE_SUFFIX,
                        "Community Report"
                )
        );

        return returnObj;
    }

    @Override
    public Page<CommReportReturn> searchRequests(MainSearchRequest searchRequest, PageRequest pageRequest) {
        Page<CommReportModel> commReport = commReportJDBCRepository.searchRequests(searchRequest, pageRequest);
        Map<String, List<String>> attachmentMap = new HashMap<>();

        if (!commReport.isEmpty()) {
            List<String> commReportIds = commReport.stream().map(BaseModel::getId).toList();
            List<CommReportAttachmentModel> attachments = commReportJDBCRepository.findByMainIdList(commReportIds);

            if (attachments != null && !attachments.isEmpty()) {
                for (CommReportAttachmentModel item : attachments) {
                    attachmentMap.computeIfAbsent(item.getReportId(), k -> new ArrayList<>()).add(item.getFilePath());
                }
            }
        }

        return commReport.map(report -> {
            CommReportReturn dto = new CommReportReturn(report);
            Optional<UsersModel> assignee = usersJDBCRepository.findById(report.getAssigneeId());
            dto.setAssigneeNmAndPosition(assignee.isPresent() ? assignee.get().getFullNmWithPosition() : "");
            dto.setAttachmentUrls(attachmentMap.getOrDefault(report.getId(), Collections.emptyList()));
            return dto;
        });
    }

    @Override
    public List<KeyValueModelStr> getBrgyOfficialList() {
        List<KeyValueModelStr> returnList = new ArrayList<>();
        List<UsersModel> officials = usersJDBCRepository.findAllBrgyOfficials();
        if (officials!=null && !officials.isEmpty()) {
            returnList = officials.stream().map(u -> new KeyValueModelStr(u.getId(), u.getFullNmWithPosition())).toList();
        }
        return returnList;
    }

    @Override
    public CommReportReturn update(EnrollmentRequest request) {
        CommReportModel modelObj = new CommReportModel(request);
        modelObj.setErrorList(new ArrayList<>());
        Optional<UsersModel> requestor = Optional.of(new UsersModel());
        Optional<UsersModel> assignedAuth = Optional.of(new UsersModel());

        if (modelObj.getId()==null) {
            modelObj.getErrorList().add("Request cannot be processed at this time as an error occurred.");
            throwErrorMessages(modelObj.getErrorList());
        }else{
            if (modelObj.getAssigneeId()==null) {
                modelObj.getErrorList().add("Assignee" + IS_REQUIRED_SUFFIX);
            }
            if (modelObj.getStatus()==null) {
                modelObj.getErrorList().add("Status" + IS_REQUIRED_SUFFIX);
            }

            if (modelObj.getStatus()!=null && SystemStatusEnum.CLOSED.getKey().equals(modelObj.getStatus()) && (modelObj.getRemarks()==null || modelObj.getRemarks().isEmpty())) {
                modelObj.getErrorList().add("Remarks" + IS_REQUIRED_SUFFIX);
                throwErrorMessages(modelObj.getErrorList());
            }

            Optional<CommReportModel> tempObj = commReportJDBCRepository.findById(modelObj.getId());
            if (tempObj.isEmpty()) {
                modelObj.getErrorList().add("The request is invalid and cannot be processed at this time.");
                throwErrorMessages(modelObj.getErrorList());
            }else{
                requestor = usersJDBCRepository.findById(tempObj.get().getUserId());
                assignedAuth = usersJDBCRepository.findById(modelObj.getAssigneeId());
            }
        }

        if (requestor.isEmpty()) modelObj.getErrorList().add("Transaction cannot be processed at this time.");

        if (modelObj.getErrorList()==null || !modelObj.getErrorList().isEmpty()) throwErrorMessages(modelObj.getErrorList());

        commReportJDBCRepository.updateReport(modelObj);

        String statusString = SystemStatusEnum.CLOSED.getKey().equals(modelObj.getStatus()) ? " is now closed." : " has been updated.";
        String msg = "Hi, " + requestor.get().getFirstNm()  + "! Your Community Report with Reference No.: " + modelObj.getRefNo() + statusString
                + "\n\nAssignee: " + (assignedAuth.isPresent() ? assignedAuth.get().getFullNm() : "") + "\nRemarks: " + modelObj.getRemarks();
        SmsModel sms = new SmsModel();
        sms.setRecipient(request.getFormattedMobileNo());
        sms.setMessage(msg);
        smsService.sendSms(sms);

        NotifLogsModel notifLogsModel = new NotifLogsModel(); //saving notif logs
        notifLogsModel.setRefNo(generateReferenceNumber(null));
        notifLogsModel.setUserId(modelObj.getUserId());
        notifLogsModel.setMessage(sms.getMessage());
        notifLogsModel.setRecipient(requestor.get().getFullNm());
        notifLogsModel.setIsSmsEmail(YesOrNoEnum.YES.getKey());
        notifLogsModel.setSentDt(new Date());
        notifLogsModel.setType(LogsTypeEnum.COMM_REPORT.getKey());
        notifLogsModel.setStatus(AlertStatusEnum.Normal.getKey());
        notifLogsModel.setOtherDetail(LogsTypeEnum.COMM_REPORT.getDesc() + " - " + SystemStatusEnum.getDscpByKey(modelObj.getStatus()));
        notifLogsModel.setMainActionStr(LogsTypeEnum.COMM_REPORT.getSecAction());
        notifLogsJDBCRepository.saveNotifLogs(notifLogsModel);

        if (requestor.get().getEmailAddress()!=null) {
            emailService.sendSimpleEmailNotif(requestor.get().getEmailAddress(), "Community Report Update", msg);
        }

        CommReportReturn returnObj = new CommReportReturn(modelObj);
        returnObj.setAssigneeNmAndPosition(assignedAuth.map(UsersModel::getFullNmWithPosition).orElse(null));
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_SINGLE_SUFFIX,
                StringMessagesUtil.COMM_REPORT
        ));

        return returnObj;
    }
}