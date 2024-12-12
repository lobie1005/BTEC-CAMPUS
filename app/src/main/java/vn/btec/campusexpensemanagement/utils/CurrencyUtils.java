<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/utils/CurrencyUtils.java
package vn.btec.campusexpensemanagement.utils;
=======
package com.btec.fpt.campus_expense_manager.utils;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/utils/CurrencyUtils.java

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    private static final Locale DEFAULT_LOCALE = Locale.US;

    public static String formatCurrency(double amount) {
        return formatCurrency(amount, DEFAULT_LOCALE);
    }

    public static String formatCurrency(double amount, Locale locale) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    public static double parseCurrency(String currencyString) {
        return parseCurrency(currencyString, DEFAULT_LOCALE);
    }

    public static double parseCurrency(String currencyString, Locale locale) {
        try {
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            Number parsedNumber = currencyFormatter.parse(currencyString);
            return parsedNumber != null ? parsedNumber.doubleValue() : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
