package com.practical.assignment.service;

import com.practical.assignment.config.AppConfig;
import com.practical.assignment.errors.UserValidationException;
import com.practical.assignment.model.UserDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

@Component
public class UserDtoValidator {
    private final AppConfig config;

    public UserDtoValidator(AppConfig config) {
        this.config = config;
    }

    public void validate(UserDto user) {
        if (user.getBirthDate() != null) {
            birthIsValid(user.getBirthDate());
        }
        if (user.getEmail() != null) {
            emailIsValid(user.getEmail());
        }

    }

    private void emailIsValid(String value) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        if (!pattern.matcher(value).matches()) {
            throw new UserValidationException("Invalid email format");
        }
    }

    private void birthIsValid(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        int age = period.getYears();
        if (age < config.getMinimumUserAge()) {
            throw new UserValidationException("User must be over " + config.getMinimumUserAge() + " years old");
        }
    }
}
