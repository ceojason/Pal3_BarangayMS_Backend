package com.javaguides.sps.helper;

public final class StringMessagesUtils {

    private StringMessagesUtils() {}

    public static final String SAVED_SINGLE_SUFFIX = "{} was successfully enrolled.";
    public static final String UPDATED_SINGLE_SUFFIX = "{} was successfully updated.";
    public static final String DELETED_SINGLE_SUFFIX = "{} was successfully deleted.";

    public static final String SAVED_MULTI_SUFFIX = "{} were successfully enrolled.";
    public static final String UPDATED_MULTI_SUFFIX = "{} were successfully updated.";
    public static final String DELETED_MULTI_SUFFIX = "{} were successfully deleted.";

    public static final String NO_STUDENT_LRN_WAS_FOUND = "No student record with LRN {} was found.";
    public static final String NO_SUBJECTS_WERE_FOUND = "No subjects found. Please select subjects to enroll.";

    public static final String IS_REQUIRED_SUFFIX = "{} is required.";
    public static final String INVALID_OBJ = "Invalid {}.";



    /////////////////////// object || variable names ///////////////////////
    public static final String FIRST_NM = "First Name";
    public static final String MIDDLE_NM = "Middle Name";
    public static final String LAST_NM = "Last Name";
    public static final String GENDER = "Gender";
    public static final String HOME_ADDRESS = "Home Address";
    public static final String EMAIL = "Email Address";
    public static final String MOBILE_NO = "Mobile Number";
    public static final String BIRTHDAY = "Birthday";


    ///////////////////////////////////////////////////////////////////////

    public static String formatMsgString(String template, String... args) {
        for (String arg : args) {
            template = template.replaceFirst("\\{\\}", arg);
        }
        return template;
    }
}
