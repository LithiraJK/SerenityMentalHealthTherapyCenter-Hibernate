package lk.ijse.gdse.serenitymentalhealththerapycenter.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    private static final Pattern NAME_PATTERN = Pattern.compile("^(Mr\\.|Mrs\\.|Ms\\.|Dr\\.)?\\s?[A-Za-z]+(?:\\s[A-Za-z]+)*$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[A-Za-z0-9\\s,./-]{5,100}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^07[01245678]\\d{7}$");
    private static final Pattern TEXT_PATTERN = Pattern.compile("^[A-Za-z0-9.,!?\\s'-]{1,300}$");
    private static final Pattern TIME_PATTERN = Pattern.compile("(?i)\\b(0[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)\\b");


    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("^P\\d{3}$");
    private static final Pattern THERAPY_SESSION_ID_PATTERN = Pattern.compile("^TS\\d{3}$");
    private static final Pattern THERAPY_PROGRAM_ID_PATTERN = Pattern.compile("^TP\\d{3}$");
    private static final Pattern THERAPIST_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern USER_ID_PATTERN = Pattern.compile("^U\\d{3}$");
    private static final Pattern PAYMENT_ID_PATTERN = Pattern.compile("^PAY-\\d{4}$");

    private static final Pattern FEES_PATTERN = Pattern.compile("^[1-9]\\d{0,6}(\\.\\d{2})?$"); // e.g., 10000.00 or 20000
    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "^\\d+\\s+(day|days|week|weeks|month|months|year|years)$", Pattern.CASE_INSENSITIVE
    );

    public static boolean isValidId(String id, String type) {
        if (id == null || type == null) return false;

        Pattern pattern;
        switch (type.toUpperCase()) {
            case "PATIENT":
                pattern = PATIENT_ID_PATTERN;
                break;
            case "THERAPY_SESSION":
                pattern = THERAPY_SESSION_ID_PATTERN;
                break;
            case "THERAPY_PROGRAM":
                pattern = THERAPY_PROGRAM_ID_PATTERN;
                break;
            case "THERAPIST":
                pattern = THERAPIST_ID_PATTERN;
                break;
            case "USER":
                pattern = USER_ID_PATTERN;
                break;
            case "PAYMENT":
                pattern = PAYMENT_ID_PATTERN;
                break;
            default:
                return false;
        }
        return pattern.matcher(id).matches();
    }



    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidText(String text) {
        return text != null && TEXT_PATTERN.matcher(text).matches();
    }

    public static boolean isValidTime(String time) {
        return time != null && TIME_PATTERN.matcher(time).matches();
    }

    public static boolean isValidFee(String fee) {
        return fee != null && FEES_PATTERN.matcher(fee).matches();
    }

    public static boolean isValidDuration(String duration) {
        return duration != null && DURATION_PATTERN.matcher(duration.trim()).matches();
    }


}
