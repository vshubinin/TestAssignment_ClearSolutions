package com.practical.assignment.service;

import com.practical.assignment.model.User;
import com.practical.assignment.model.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(User user);

    User update(User user);

    User partialUpdate(UserDto user);

    void delete(UUID userId);

    List<User> searchByBirthDateRange(LocalDate fromDate, LocalDate toDate);

    User findById(UUID userId);
}
