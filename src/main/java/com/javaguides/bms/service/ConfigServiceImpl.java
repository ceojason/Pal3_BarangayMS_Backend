package com.javaguides.bms.service;

import com.javaguides.bms.enums.DocumentCategoryEnum;
import com.javaguides.bms.enums.DocumentSubCatEnum;
import com.javaguides.bms.enums.SystemConfigEnum;
import com.javaguides.bms.helper.StringMessagesUtil;
import com.javaguides.bms.jdbc.repository.NotifLogsJDBCRepository;
import com.javaguides.bms.jdbc.repository.SystemConfigJDBCRepository;
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
    public ConfigReturnModel validateAndUpdate(ConfigRequest requestObj) {
        ConfigReturnModel returnObj = new ConfigReturnModel();
        List<String> errors = new ArrayList<>();
        String refNo = null;

        if (requestObj!=null) {
            if (requestObj.getConfigCd()!=null) {
                if (requestObj.getConfigCd().equals(SystemConfigEnum.PRICING_SETTINGS.getCode())) {
                    refNo = mapToPricingAndProcessUpdate(requestObj);
                }
            }else{
                errors.add("Please select a service.");
            }
        }else{
            errors.add("An error occurred. Transaction cannot be processed.");
        }

        if (!errors.isEmpty()) throwErrorMessages(errors);
        returnObj.setRefNo(refNo);
        returnObj.setAckMessage(StringMessagesUtil.formatMsgString(
                StringMessagesUtil.UPDATED_MULTI_SUFFIX,
                StringMessagesUtil.PRICING
        ));

        return returnObj;
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

        return generateReferenceNumber(null);
    }

    private FeePricingModel buildModel(Integer docSubCatKey, BigDecimal fee) {
        FeePricingModel model = new FeePricingModel();
        model.setDocSubCatKey(docSubCatKey);
        model.setProcessFee(fee);
        return model;
    }

}
