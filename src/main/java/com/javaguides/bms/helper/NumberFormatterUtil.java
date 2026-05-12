package com.javaguides.bms.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberFormatterUtil {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static String format(BigDecimal fee) {
        if (fee == null) return "0.00";
        return df.format(fee);
    }
}
