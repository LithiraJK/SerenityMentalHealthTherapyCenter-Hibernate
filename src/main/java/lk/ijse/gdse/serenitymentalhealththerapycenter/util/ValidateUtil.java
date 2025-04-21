package lk.ijse.gdse.serenitymentalhealththerapycenter.util;

import java.util.regex.Pattern;

public class ValidateUtil {

    private static final Pattern NAME_PATTERN = Pattern.compile("^(Mr\\.|Mrs\\.|Ms\\.|Dr\\.)?\\s?[A-Za-z]+(?:\\s[A-Za-z]+)*$");

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[A-Za-z0-9\\s,./-]{5,100}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^07[01245678]\\d{7}$");

    private static final Pattern TEXT_PATTERN = Pattern.compile("^[A-Za-z0-9.,!?\\s'-]{1,300}$");

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
}
