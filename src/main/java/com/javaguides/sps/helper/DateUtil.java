package com.javaguides.sps.helper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateUtil {

    public static String getDateStringWithFormat(Date dt, String dtFormat) {
        if (dt == null || dtFormat == null || dtFormat.trim().isEmpty()) return null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dtFormat);
            return sdf.format(dt);
        } catch (IllegalArgumentException e) {
            return "Invalid format.";
        }
    }

    public static boolean checkValidDateFrom(Date dateVal, int yearsAgo) {
        if (dateVal == null) {
            return false;
        }

        LocalDate inputDate = dateVal.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate cutoffDate = LocalDate.now().minusYears(yearsAgo);
        return !inputDate.isAfter(cutoffDate);
    }

}
