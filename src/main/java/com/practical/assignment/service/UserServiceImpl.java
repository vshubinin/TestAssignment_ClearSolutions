package com.practical.assignment.service;

import com.practical.assignment.errors.UserNotFoundException;
import com.practical.assignment.model.User;
import com.practical.assignment.model.UserDto;
import com.practical.assignment.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoValidator dtoValidator;

    public UserServiceImpl(UserRepository userRepository, UserDtoValidator dtoValidator) {
        this.userRepository = userRepository;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public User create(User user) {
        if (user.getAddress() == null) {
            user.setAddress("");
        }
        if (user.getPhoneNumber() == null) {
            user.setPhoneNumber("");
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User updatedUser) {
        findById(updatedUser.getId());
        return userRepository.update(updatedUser);
    }

    @Override
    public User partialUpdate(UserDto user) {
        dtoValidator.validate(user);
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + user.getId() + " not found"));
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(existingUser.getLastName());
        }
        if (user.getAddress() != null) {
            existingUser.setAddress(user.getAddress());
        }
        if (user.getPhone() != null) {
            existingUser.setPhoneNumber(user.getPhone());
        }

        return userRepository.update(existingUser);
    }

    @Override
    public void delete(UUID userId) {
        findById(userId);
        userRepository.delete(userId);
    }

    @Override
    public List<User> searchByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new ValidationException("From date must be before To date");
        }
        return userRepository.getByBirthDateRange(fromDate, toDate);
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }
}
