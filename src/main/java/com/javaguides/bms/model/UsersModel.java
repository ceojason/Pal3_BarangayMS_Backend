package com.javaguides.bms.model;

import com.javaguides.bms.customannotations.TableAlias;
import com.javaguides.bms.enums.BrgyPositionEnum;
import com.javaguides.bms.enums.DateFormatEnum;
import com.javaguides.bms.enums.PhaseEnum;
import com.javaguides.bms.enums.ResidentClassificationEnum;
import com.javaguides.bms.helper.DateUtil;
import com.javaguides.bms.helper.KeyValueModel;
import com.javaguides.bms.model.basemodel.BaseModel;
import com.javaguides.bms.model.requestmodel.EnrollmentRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Table(name="tbl_users")
@TableAlias("tu")
public class UsersModel extends BaseModel {

    @Column(name = "REF_NO")
    private String refNo;

    @Column(name = "FIRST_NM")
    private String firstNm;

    @Column(name = "MIDDLE_NM")
    private String middleNm;

    @Column(name = "LAST_NM")
    private String lastNm;

    @Column(name = "SUFFIX")
    private String suffix;

    @Column(name = "BIRTH_DT")
    private Date birthDt;

    @Column(name = "BIRTH_PLACE")
    private String birthPlace;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "CIVIL_STATUS_KEY")
    private Integer civilStatusKey;

    @Column(name = "MOBILE_NO")
    private String mobileNo;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "PHASE_KEY")
    private Integer phaseKey;

    @Column(name = "BLOCK")
    private String block;

    @Column(name = "LOT")
    private String lot;

    @Column(name = "STREET")
    private String street;

    @Column(name = "HOUSEHOLD_KEY")
    private String householdKey;

    @Column(name = "OCCUPATION")
    private String occupation;

    @Column(name = "RELIGION")
    private String religion;

    @Column(name = "IS_REGISTERED_VOTER")
    private Integer isRegisteredVoter;

    @Column(name = "IS_HOUSEHOLD_HEAD")
    private Integer isHouseholdHead;

    @Column(name = "BRGY_POSITION_KEY")
    private Integer brgyPositionKey;

    @Column(name = "CLASSIFICATION_KEY")
    private String classificationKey;

    @Column(name = "DATE_ENROLLED")
    private Date dateEnrolled;

    @Transient
    private String userId;

    @Transient
    private String cd;

    @Transient
    private String householdDesc;

    public String formattedMobileNo() {
        if (mobileNo!=null && !mobileNo.isEmpty()) {
            return "+63" + mobileNo.substring(1);
        }
        return "";
    }

    @Transient
    private List<Integer> residentClassKeys;

    @Transient
    private Integer isBrgyOfficial;

    @Transient
    private String brgyPositionKeyString;

    @Transient
    private String genderDscp;

    @Transient
    private String isHouseholdHeadString;

    @Transient
    private String password;

    @Transient
    private String birthDtString;

    @Transient
    private String civilStatusString;

    @Transient
    private String phaseString;

    @Transient
    private String householdString;

    @Transient
    private String householdKeyDscp;

    @Transient
    private String tempHouseholdForSave;

    @Transient
    private String tempUniqueKey;

    @Transient
    private String dateEnrolledString;

    @Transient
    private String isRegisteredVoterString;

    @Transient
    private String addressFromConfig;

    public String getHomeAddress() {
        String defaultBrgyNmCityAndProv = "PALIPARAN III DASMARIÑAS CITY, CAVITE";
        String defaultZipCd = "4114";
        boolean appendDefaultAddressString = false;

        StringBuilder address = new StringBuilder();
        if (block!=null) address.append(block).append(" ");
        if (lot!=null) address.append(lot).append(" ");
        if (street!=null) address.append(street).append(" ");
        if (phaseKey!=null) address.append(PhaseEnum.getDesc2ByKey(phaseKey)).append(" ");

        if (appendDefaultAddressString) {
            address.append(defaultBrgyNmCityAndProv).append(" ");
            address.append(defaultZipCd);
        }else{
            address.append(addressFromConfig!=null ? addressFromConfig : "");
        }

        return address.toString();
    }

    public String getDateEnrolledString() {
        return dateEnrolled!=null
                ? DateUtil.getDateStringWithFormat(dateEnrolled, DateFormatEnum.DT_FORMAT_1.getPattern()) : "";
    }

    public String getClassificationKeyString() {
        StringBuilder sb = new StringBuilder();
        if (residentClassKeys!=null && !residentClassKeys.isEmpty()) {
            for (Integer i : residentClassKeys) {
                if (!sb.isEmpty()) sb.append("|");
                sb.append(i);
            }
        }
        return sb.toString();
    }

    public String getClassificationKeyStringForDisplay() {
        StringBuilder sb = new StringBuilder();
        List<Integer> tempKeys = new ArrayList<>();

        if (residentClassKeys!=null && !residentClassKeys.isEmpty()) {
            tempKeys = residentClassKeys;
        }
        else if (classificationKey!=null && !classificationKey.isEmpty()) {
            tempKeys = getClassificationKeyList();
        }

        if (tempKeys!=null && !tempKeys.isEmpty()) {
            for (Integer i : tempKeys) {
                if (!sb.isEmpty()) sb.append(", ");
                sb.append(ResidentClassificationEnum.getDescByKey(i));
            }
        }
        return sb.toString();
    }

    public String getFullNm() {
        StringBuilder fullNm = new StringBuilder();
        if (lastNm!=null) {
            fullNm.append(lastNm);
        }
        if (firstNm!=null) {
            fullNm.append(", ")
                    .append(firstNm);
        }
        if (middleNm!=null) {
            fullNm.append(", ")
                    .append(middleNm);
        }
        if (suffix!=null) {
            fullNm.append(" ")
                    .append(suffix);
        }
        return fullNm.toString();
    }

    public String getFullNmWithPosition() {
        String position = brgyPositionKey!=null ? " - " + BrgyPositionEnum.getDescByKey(brgyPositionKey) : "";
        return getFullNm() + position;
    }

    public String getFullNm2() {
        StringBuilder fullNm = new StringBuilder()
                .append(firstNm!=null ? firstNm : "")
                .append(" ")
                .append(middleNm!=null ? middleNm + " " : "")
                .append(lastNm)
                .append(suffix!=null ? " " + suffix : "");
        return fullNm.toString();
    }

    public List<Integer> getClassificationKeyList() {
        List<Integer> keys = new ArrayList<>();
        if (classificationKey!=null) {
            keys = Arrays.stream(classificationKey.split("\\|"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return keys;
    }

    public List<KeyValueModel> getKeyValueForResidentKeys() {
        List<KeyValueModel> returnValue = new ArrayList<>();
        List<Integer> keys = getClassificationKeyList();
        if (keys!=null && !keys.isEmpty()) {
            for (Integer key : keys) {
                returnValue.add(new KeyValueModel(key, ResidentClassificationEnum.getDescByKey(key)));
            }
        }
        return returnValue;
    }

    public UsersModel(EnrollmentRequest request) {
        if (request!=null) {
            setId(request.getId());
            setFirstNm(request.getFirstNm());
            setMiddleNm(request.getMiddleNm());
            setLastNm(request.getLastNm());
            setSuffix(request.getSuffix());
            setBirthDt(request.getBirthDt());
            setBirthPlace(request.getBirthPlace());
            setDateEnrolled(request.getDateEnrolled());
            setDateEnrolledString(request.getDateEnrolledString());
            setGender(request.getGender());
            setCivilStatusKey(request.getCivilStatusKey());
            setMobileNo(request.getMobileNo());
            //setFormattedMobileNo(request.getFormattedMobileNo());
            setEmailAddress(request.getEmailAddress());
            setPhaseKey(request.getPhaseKey());
            //setHomeAddress(request.getHomeAddress());
            setBlock(request.getBlock());
            setLot(request.getLot());
            setStreet(request.getStreet());
            setIsHouseholdHead(request.getIsHouseholdHead());
            setHouseholdKey(request.getHouseholdKey());
            setHouseholdKeyDscp(request.getHouseholdKeyDscp());
            setIsBrgyOfficial(request.getIsBrgyOfficial());
            setBrgyPositionKey(request.getBrgyPositionKey());
            setTempHouseholdForSave(request.getTempHouseholdForSave());
            setOccupation(request.getOccupation());
            setReligion(request.getReligion());
            setIsRegisteredVoter(request.getIsRegisteredVoter());
            setResidentClassKeys(request.getResidentClassKeys());
            setCd(request.getCd());
            setPassword(request.getPassword());
            setRefNo(request.getRefNo());
            setStatus(request.getStatus());
        }
    }
}
