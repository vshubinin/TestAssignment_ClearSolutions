package com.practical.assignment.controller;

import com.practical.assignment.model.User;
import com.practical.assignment.repository.UserRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private String testUserId;
    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setBirthDate(LocalDate.of(2000, 1, 1));
        testUserId = userRepository.save(testUser).getId().toString();

    }

    @Test
    void testCreateUser() throws Exception {
        String userJson = "{ \"email\": \"test@example.com\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"birthDate\": \"2000-01-01\" }";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testgetuseridwithnull() throws Exception {
        mockMvc.perform(get("/users/{id}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUserId));
    }

    @Test
    void testUpdateUser() throws Exception {
        String userJson = "{ \"email\": \"updated@example.com\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"birthDate\": \"1990-01-01\", \"address\": \"123 Main St\"}";


        mockMvc.perform(put("/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    void testPartialUpdateUser() throws Exception {
        String userJson = "{ \"email\": \"updated@example.com\" }";

        mockMvc.perform(patch("/users/{id}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", testUserId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchByBirthDateRange_InvalidDateRange() {
        LocalDate fromDate = LocalDate.of(2024, 4, 28);
        LocalDate toDate = LocalDate.of(2024, 4, 27);

        assertThrows(ValidationException.class, () -> {
            userController.getUsersByBirthDateRange(fromDate, toDate);
        });
    }

    @Test
    void testGetUsersByBirthDateRange() throws Exception {
        LocalDate fromDate = LocalDate.of(2003, 1, 1);
        LocalDate toDate = LocalDate.now();

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(2003, 1, 1));
        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setBirthDate(LocalDate.of(2005, 5, 5));
        User user3 = new User();
        user3.setFirstName("Alice");
        user3.setLastName("Johnson");
        user3.setBirthDate(LocalDate.of(1998, 10, 10));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        mockMvc.perform(get("/users")
                        .param("fromDate", fromDate.toString())
                        .param("toDate", toDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].firstName").value("John"))
                .andExpect(jsonPath("$.[1].firstName").value("Jane"));
    }
}