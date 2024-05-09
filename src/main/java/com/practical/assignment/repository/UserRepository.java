package com.practical.assignment.repository;

import com.practical.assignment.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final List<User> userDb = new ArrayList<>();

    public User save(User user) {
        user.setId(UUID.randomUUID());
        userDb.add(user);
        return user;
    }

    public Optional<User> findById(UUID userId) {
        return userDb.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    public User update(User existingUser) {
        userDb.removeIf(user -> user.getId().equals(existingUser.getId()));
        userDb.add(existingUser);
        return existingUser;
    }

    public void delete(UUID userId) {
        userDb.removeIf(user -> user.getId().equals(userId));
    }

    public List<User> getByBirthDateRange(LocalDate fromDate, LocalDate toDate) {

        return userDb.stream()
                .filter(user -> user.getBirthDate().isAfter(fromDate.minusDays(1)) && user.getBirthDate().isBefore(toDate.plusDays(1)))
                .toList();
    }
}
