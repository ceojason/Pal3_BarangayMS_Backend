package com.javaguides.sps.enums;

import lombok.Getter;

@Getter
public enum DateFormatEnum {


    DT_FORMAT_1("DTFORMAT1", "MMMM d, yyyy", "August 16, 2025"),
    DT_FORMAT_2("DTFORMAT2", "MM/dd/yyyy", "08/16/2025"),
    DT_FORMAT_3("DTFORMAT3", "dd/MM/yyyy", "16/08/2025"),
    DT_FORMAT_4("DTFORMAT4", "yyyy-MM-dd", "2025-08-16"),
    DT_FORMAT_5("DTFORMAT5", "EEEE, MMM d yyyy", "Saturday, Aug 16 2025"),
    DT_FORMAT_6("DTFORMAT6", "MMM d, yyyy hh:mm a", "Aug 16, 2025 08:30 PM"),
    DT_FORMAT_7("DTFORMAT7", "MMMM d, yyyy hh:mm a", "August 16, 2025 08:30 PM"),
    DT_FORMAT_8("DTFORMAT8", "dd-MM-yyyy HH:mm", "16-08-2025 20:30"),
    DT_FORMAT_9("DTFORMAT9", "yyyy-MM-dd HH:mm:ss", "2025-08-16 20:30:45"),
    DT_FORMAT_10("DTFORMAT10", "MM/dd/yyyy hh:mm:ss a", "08/16/2025 08:30:45 PM"),
    DT_FORMAT_11("DTFORMAT11", "dd MMM yyyy hh:mm:ss a", "16 Aug 2025 08:30:45 PM"),
    DT_FORMAT_12("DTFORMAT12", "EEE, MMM d, yyyy hh:mm:ss a", "Sat, Aug 16, 2025 08:30:45 PM"),
    DT_FORMAT_13("DTFORMAT13", "EEEE, MMMM d, yyyy HH:mm", "Saturday, August 16, 2025 20:30"),
    DT_FORMAT_14("DTFORMAT14", "hh:mm a", "08:30 PM"),
    DT_FORMAT_15("DTFORMAT15", "HH:mm", "20:30"),
    DT_FORMAT_16("DTFORMAT16", "hh:mm:ss a", "08:30:45 PM"),
    DT_FORMAT_17("DTFORMAT17", "HH:mm:ss", "20:30:45")
    ;

    private final String key;
    private final String pattern;
    private final String sample;

    DateFormatEnum(String key, String pattern, String sample) {
        this.key = key;
        this.pattern = pattern;
        this.sample = sample;
    }

}
