package com.javaguides.bms.service;

import com.javaguides.bms.enums.DocumentCategoryEnum;
import com.javaguides.bms.enums.DocumentSubCatEnum;
import com.javaguides.bms.enums.SystemConfigEnum;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemConfigJDBCRepository;
import com.javaguides.bms.model.ConfigModel;
import com.javaguides.bms.model.FeePricingModel;
import com.javaguides.bms.model.requestmodel.ConfigRequest;
import com.javaguides.bms.model.returnmodel.ConfigReturnModel;
import com.javaguides.bms.service.baseservice.BaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService {

    private final NotifLogsJDBCRepository notifLogsJDBCRepository;
    private final SystemConfigJDBCRepository systemConfigJDBCRepository;

    @Override
    public List<FeePricingModel> feePricingList() {
        List<FeePricingModel> feePricingList = systemConfigJDBCRepository.pricingList();

        feePricingList = Optional.ofNullable(feePricingList).orElse(Collections.emptyList()).stream()
                .peek(fp -> {
                    fp.setDocSubCatKeyString(DocumentSubCatEnum.getDocuSubCatDescByKey(fp.getDocSubCatKey()));
                    fp.setDocCatKey(DocumentSubCatEnum.getCategoryByKey(fp.getDocSubCatKey()));
                    fp.setDocCatKeyString(DocumentCategoryEnum.getDocuCatDescByKey(fp.getDocCatKey()));

                }).toList();
        return feePricingList;
    }

    @Override
    public String getAddressConfigObj() {
        String id = SystemConfigEnum.BRGY_SETTINGS.getCode();
        Optional<ConfigModel> configObj = findConfigById(id);
        return configObj.map(ConfigModel::configAddressPrefix).orElse(null);
    }

    @Override
    public Optional<ConfigModel> findConfigById(String id) {
        Optional<ConfigModel> modelObj = Optional.of(new ConfigModel());
        if (id!=null) modelObj = systemConfigJDBCRepository.findById(id);
        return modelObj;
    }

    @Override
    public ConfigReturnModel getHotlines() {
        ConfigReturnModel returnObj = new ConfigReturnModel();
        Optional<ConfigModel> modelObj = systemConfigJDBCRepository.findById(SystemConfigEnum.EMERGENCY_HOTLINES.getCode());
        if (modelObj.isEmpty()) {
            throwErrorMessage("An error occurred. Transaction cannot be processed.");
        }else{
            ConfigModel tempModel = modelObj.get();
            returnObj.setString1(tempModel.getString_1());
            returnObj.setString2(tempModel.getString_2());
            returnObj.setString3(tempModel.getString_3());
            returnObj.setString4(tempModel.getString_4());
            returnObj.setString5(tempModel.getString_5());
            returnObj.setString6(tempModel.getString_6());
            returnObj.setString7(tempModel.getString_7());
            returnObj.setString8(tempModel.getString_8());
            returnObj.setString9(tempModel.getString_9());
            returnObj.setString10(tempModel.getString_10());
            returnObj.setConfigCd(tempModel.getId());
        }

        return returnObj;

    }

    @Override
    public ConfigReturnModel getBarangayDetails() {
        ConfigReturnModel returnObj = new ConfigReturnModel();
        Optional<ConfigModel> modelObj = systemConfigJDBCRepository.findById(SystemConfigEnum.BRGY_SETTINGS.getCode());
        if (modelObj.isEmpty()) {
            throwErrorMessage("An error occurred. Transaction cannot be processed.");
        }else{
            ConfigModel tempModel = modelObj.get();
            returnObj.setBarangayNm(tempModel.getString_1());
            returnObj.setMunicipalAddress(tempModel.getString_2());
            returnObj.setProvince(tempModel.getString_3());
            returnObj.setZipCode(tempModel.getString_4());
            returnObj.setRegion(tempModel.getString_5());
            returnObj.setCountry(tempModel.getString_6());
            returnObj.setConfigCd(tempModel.getId());
        }

        return returnObj;
    }

    @Override
    public ConfigReturnModel validateAndUpdate(ConfigRequest requestObj) {
        ConfigReturnModel returnObj = new ConfigReturnModel();
        List<String> errors = new ArrayList<>();

        String ackMsg = null;

        if (requestObj!=null) {
            if (requestObj.getConfigCd()!=null) {
                if (requestObj.getConfigCd().equals(SystemConfigEnum.PRICING_SETTINGS.getCode())) {
                    ackMsg = mapToPricingAndProcessUpdate(requestObj);
                }
                else if (requestObj.getConfigCd().equals(SystemConfigEnum.BRGY_SETTINGS.getCode())) {
                    ackMsg = mapToBrgyConfigAndProcessUpdate(requestObj);
                }
                else if (requestObj.getConfigCd().equals(SystemConfigEnum.EMERGENCY_HOTLINES.getCode())) {
                    ackMsg = mapToHotlineFieldsAndProcessUpdate(requestObj);
                }
            }else{
                errors.add("Please select a service.");
            }
        }else{
            errors.add("An error occurred. Transaction cannot be processed.");
        }

        if (!errors.isEmpty()) throwErrorMessages(errors);
        String refNo = generateReferenceNumber(null);
        returnObj.setRefNo(refNo);
        returnObj.setAckMessage(ackMsg);

        return returnObj;
    }

    private String mapToHotlineFieldsAndProcessUpdate(ConfigRequest requestObj) {
        ConfigModel modelObj = new ConfigModel();
        modelObj.setId(requestObj.getConfigCd());
        modelObj.setString_1(requestObj.getString1().trim());
        modelObj.setString_2(requestObj.getString2().trim());
        modelObj.setString_3(requestObj.getString3().trim());
        modelObj.setString_4(requestObj.getString4().trim());
        modelObj.setString_5(requestObj.getString5().trim());
        modelObj.setString_6(requestObj.getString6().trim());
        modelObj.setString_7(requestObj.getString7().trim());
        modelObj.setString_8(requestObj.getString8().trim());
        modelObj.setString_9(requestObj.getString9().trim());
        modelObj.setString_10(requestObj.getString10().trim());
        modelObj.setStatus(null);
        systemConfigJDBCRepository.updateConfig(modelObj);

        return StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_MULTI_SUFFIX,
                StringMessagesUtil.EMERGENCY_HOTLINES);
    }

    private String mapToBrgyConfigAndProcessUpdate(ConfigRequest requestObj) {
        ConfigModel modelObj = new ConfigModel();
        modelObj.setId(requestObj.getConfigCd());
        modelObj.setString_1(requestObj.getBarangayNm().trim());
        modelObj.setString_2(requestObj.getMunicipalAddress().trim());
        modelObj.setString_3(requestObj.getProvince().trim());
        modelObj.setString_4(requestObj.getZipCode().trim());
        modelObj.setString_5(requestObj.getRegion().trim());
        modelObj.setString_6(requestObj.getCountry().trim());
        modelObj.setStatus(null);
        systemConfigJDBCRepository.updateConfig(modelObj);

        return StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_MULTI_SUFFIX,
                StringMessagesUtil.BARANGAY_DTLS);
    }

    private String mapToPricingAndProcessUpdate(ConfigRequest requestObj) {
        List<FeePricingModel> pricingModels = new ArrayList<>();
        pricingModels.add(buildModel(1, requestObj.getFee001()));
        pricingModels.add(buildModel(2, requestObj.getFee002()));
        pricingModels.add(buildModel(3, requestObj.getFee003()));
        pricingModels.add(buildModel(4, requestObj.getFee004()));
        pricingModels.add(buildModel(5, requestObj.getFee005()));
        pricingModels.add(buildModel(6, requestObj.getFee006()));
        pricingModels.add(buildModel(7, requestObj.getFee007()));
        pricingModels.add(buildModel(8, requestObj.getFee008()));
        pricingModels.add(buildModel(9, requestObj.getFee009()));
        pricingModels.add(buildModel(10, requestObj.getFee010()));
        pricingModels.add(buildModel(11, requestObj.getFee011()));
        pricingModels.add(buildModel(12, requestObj.getFee012()));
        pricingModels.add(buildModel(13, requestObj.getFee013()));
        pricingModels.add(buildModel(14, requestObj.getFee014()));
        pricingModels.add(buildModel(15, requestObj.getFee015()));
        pricingModels.add(buildModel(16, requestObj.getFee016()));
        pricingModels.add(buildModel(17, requestObj.getFee017()));
        pricingModels.add(buildModel(18, requestObj.getFee018()));
        pricingModels.add(buildModel(19, requestObj.getFee019()));
        pricingModels.add(buildModel(20, requestObj.getFee020()));
        pricingModels.add(buildModel(21, requestObj.getFee021()));
        pricingModels.add(buildModel(22, requestObj.getFee022()));
        pricingModels.add(buildModel(23, requestObj.getFee023()));
        pricingModels.add(buildModel(24, requestObj.getFee024()));
        pricingModels.add(buildModel(25, requestObj.getFee025()));
        pricingModels.add(buildModel(26, requestObj.getFee026()));
        pricingModels.removeIf(p -> p.getProcessFee()==null);

        pricingModels.forEach(systemConfigJDBCRepository::updateFeePricing);

        return StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_MULTI_SUFFIX,
                StringMessagesUtil.PRICING);
    }

    private FeePricingModel buildModel(Integer docSubCatKey, BigDecimal fee) {
        FeePricingModel model = new FeePricingModel();
        model.setDocSubCatKey(docSubCatKey);
        model.setProcessFee(fee!=null ? fee : BigDecimal.valueOf(0));
        return model;
    }

}
