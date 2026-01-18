package com.javaguides.bms.service.baseservice;

import com.javaguides.bms.enums.ServicesEnum;
import com.javaguides.bms.enums.SmsTypeEnum;
import com.javaguides.bms.helper.ErrorException;
import com.javaguides.bms.model.basemodel.SmsModel;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
public abstract class BaseServiceImpl implements BaseService {

    public void throwErrorMessages(List<String> errorList) {
        throw new ErrorException(errorList);
    }

    public void throwErrorMessage(String error) {
        throw new ErrorException(Arrays.asList(error));
    }

    public void checkIfOnlyNumber(String input, String fieldNm, List<String> errorList) {
        if (input!=null && !input.matches("\\d+")) {
            errorList.add(buildMessage("Error: %s has invalid character/s.", fieldNm));
        }
    }

    public void checkIfAlphanumericSp(String input, String fieldNm, List<String> errorList) {
        if (input!=null && !input.matches("[a-zA-Z0-9 ]+")) {
            errorList.add(buildMessage("Error: %s has invalid character/s.", fieldNm));
        }
    }

    public void checkIfAlphanumeric(String input, String fieldNm, List<String> errorList) {
        if (input!=null && !input.matches("[a-zA-Z0-9]+")) {
            errorList.add(buildMessage("Error: %s has invalid character/s.", fieldNm));
        }
    }

    public void checkIfHasSpecialChars(String input, String fieldNm, List<String> errorList) {
        if (input!=null && !input.matches(".*[^a-zA-Z0-9 ].*")) {
            errorList.add(buildMessage("Error: %s has invalid character/s.", fieldNm));
        }
    }

    public void maxStringCharCounter(String strToValidate, int maxLength, String fieldNm, List<String> errorList) {
        if (strToValidate!=null && strToValidate.length()>maxLength) {
            errorList.add(buildMessage("Error: %s has a maximum length of %d.", fieldNm, maxLength));
        }
    }

    public void minStringCharCounter(String strToValidate, int minLength, String fieldNm, List<String> errorList) {
        if (strToValidate!=null && strToValidate.length()<minLength) {
            errorList.add(buildMessage("Error: %s has a minimum length of %d.", fieldNm, minLength));
        }
    }

    public static String buildMessage(String format, Object... args) {
        try {
            return String.format(format, args);
        } catch (IllegalFormatException e) {
            return "Invalid format string or arguments.";
        }
    }

    public static String generateReferenceNumber(String refNoCode) {
        String refNoPrefix = refNoCode!=null ? ServicesEnum.getRefNoCodeByCd(refNoCode) : "BMS";
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        Random random = new Random();
        int randomDigits = 100000 + random.nextInt(90000000);
        return refNoPrefix + "-" + timestamp + "-" + randomDigits;
    }

    public String getStringFieldValue(Object obj, String fieldName) throws IllegalAccessException {
        Field field = getFieldByNameIgnoreCase(obj.getClass(), fieldName);
        if (field == null) return null;

        field.setAccessible(true);
        Object value = field.get(obj);
        return value != null ? value.toString() : null;
    }


    public static Field getFieldByNameIgnoreCase(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass(); // Also check superclasses like BaseModel
        }
        return null;
    }

    public static String generateMessage(SmsModel modelObj) {
        StringBuilder body = new StringBuilder();
//        if (modelObj!=null && modelObj.getSmsType().equals(SmsTypeEnum.NEW_USER_SMS.getKey())) {
//            body.append("Welcome to eBarangayConnect!").append(" ");
//            body.append("User ID: ").append(modelObj.getParam1()).append(" ");
//            body.append("Password: ").append(modelObj.getParam2());
//        }
        return body.toString();
    }

}
