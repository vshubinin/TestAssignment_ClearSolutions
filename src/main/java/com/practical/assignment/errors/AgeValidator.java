package com.practical.assignment.errors;

import com.practical.assignment.config.AppConfig;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<Age, LocalDate> {

    private final AppConfig appConfig;

    public AgeValidator(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        Period period = Period.between(value, today);
        String minAgeMessage = "User must be over " + appConfig.getMinimumUserAge() + " years old";
        context.buildConstraintViolationWithTemplate(minAgeMessage).addConstraintViolation();

        return period.getYears() >= appConfig.getMinimumUserAge();
    }
}
