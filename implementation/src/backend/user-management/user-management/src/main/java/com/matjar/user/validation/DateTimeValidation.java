package com.matjar.user.validation;

import com.matjar.user.exception.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.regex.Pattern;

public class DateTimeValidation {

    private static final String DATE_PATTERN = "^\\d{4}--(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    public static void validateDateFormat(String dateStr) {
        if (!Pattern.matches(DATE_PATTERN, dateStr))
            throw new ValidationException("Invalid dob date, date must be on format yyyy-MM-dd");
    }

    public static Date parseDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setLenient(false); // Ensure strict parsing
        try {
            return formatter.parse(dateStr);
        }
        catch (ParseException e) {
            throw new ValidationException("Invalid dob date, date must be on format yyyy-MM-dd");
        }
    }

    public static Date validateDob(String date) {
        parseDate(date);
        return parseDate(date);
    }

}
