<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/ValidationUtils.java
package vn.btec.campusexpensemanagement.utils;
=======
package com.btec.fpt.campus_expense_manager.utils;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/ValidationUtils.java

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class ValidationUtils {
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"
    );

    // Password validation: At least 8 characters, 1 uppercase, 1 lowercase, 1 number
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
    );

    // Date formats to validate
    private static final String[] DATE_FORMATS = {
        "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd", "dd-MM-yyyy"
    };

    /**
     * Validate email address
     * @param email Email to validate
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/ValidationUtils.java
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
=======
        return !TextUtils.isEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/ValidationUtils.java
    }

    /**
     * Validate password
     * @param password Password to validate
     * @return true if password meets complexity requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/ValidationUtils.java
        return !TextUtils.isEmpty(password) && password.length() >= 6;
=======
        return !TextUtils.isEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/ValidationUtils.java
    }

    /**
     * Validate description
     * @param description Description to validate
     * @return true if description is not empty and within reasonable length
     */
    public static boolean isValidDescription(String description) {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/ValidationUtils.java
        return !TextUtils.isEmpty(description) && description.length() <= 500;
=======
        return !TextUtils.isEmpty(description) && description.length() <= 200;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/ValidationUtils.java
    }

    /**
     * Validate amount
     * @param amountStr Amount as string
     * @return true if amount is a valid positive number
     */
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/ValidationUtils.java
    public static boolean isValidAmount(String amount) {
        if (TextUtils.isEmpty(amount)) return false;
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
=======
    public static boolean isValidAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            return amount > 0;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/ValidationUtils.java
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate date string
     * @param dateStr Date to validate
     * @return true if date is valid and in an acceptable format
     */
    public static boolean isValidDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return false;
        }

        for (String format : DATE_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                sdf.setLenient(false);
                Date date = sdf.parse(dateStr);
                
                // Ensure date is not in the future
                return date != null && !date.after(new Date());
            } catch (ParseException e) {
                // Try next format
            }
        }
        return false;
    }

    /**
     * Validate name
     * @param name Name to validate
     * @return true if name is not empty and contains only letters and spaces
     */
    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && 
               name.matches("^[a-zA-Z\\s]+$") && 
               name.length() >= 2 && 
               name.length() <= 50;
    }
}
