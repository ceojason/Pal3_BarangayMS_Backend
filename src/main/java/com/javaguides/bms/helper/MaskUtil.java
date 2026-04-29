package com.javaguides.bms.helper;

public class MaskUtil {

    public static String maskAll(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return "*".repeat(input.length());
    }

    public static String maskKeepFirst(String input, int visibleCount) {
        if (input == null || input.length() <= visibleCount) {
            return input;
        }

        return input.substring(0, visibleCount) + "*".repeat(input.length() - visibleCount);
    }

    public static String maskMiddle(String input, int keepStart, int keepEnd) {
        if (input == null || input.length() <= (keepStart + keepEnd)) {
            return input;
        }

        String start = input.substring(0, keepStart);
        String end = input.substring(input.length() - keepEnd);
        int maskLength = input.length() - (keepStart + keepEnd);

        return start + "*".repeat(maskLength) + end;
    }
}
