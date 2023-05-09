package dev.lyneapp.backend.matching.utils;

import java.time.LocalDate;
import java.time.Period;

public class Utils {
    public int calculateUserAge(LocalDate birthDate, LocalDate currentDate) {

        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}