package com.matjar.usermanagementapi.utils;

import java.time.LocalDate;
import java.time.Period;

public class DateTimeHandler {

    public static int calculateAge(LocalDate dob) {
        Period period = Period.between(dob, LocalDate.now());
        return period.getYears();
    }

}
