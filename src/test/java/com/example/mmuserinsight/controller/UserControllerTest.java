package com.example.mmuserinsight.controller;

import com.example.mmuserinsight.dto.User;
import com.example.mmuserinsight.service.CSVFileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private CSVFileService csvFileService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserById_Success() throws IOException {
        // Arrange
        int userId = 1;
        User user = new User(userId, "Peter", "Parker", "peter.parker@esu.edu", "physicist",
                "2021-12-17", "USA", "New York");
        when(csvFileService.getUserById(userId)).thenReturn(user);

        // Act
        ResponseEntity<User> response = userController.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserById_Fail() throws IOException {
        // Arrange
        int userId = 1;
        User user = new User(userId, "Tony", "Stark", "tony.stark@starkindustries.com", "CEO and Founder",
                "2022-05-01", "USA", "New York");
        when(csvFileService.getUserById(userId)).thenReturn(new User());

        // Act
        ResponseEntity<User> response = userController.getUserById(userId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUsersCreatedBetween_Success() {
        // Arrange
        LocalDate startDate = LocalDate.parse("1942-01-01");
        LocalDate endDate = LocalDate.parse("2008-12-31");
        User user1 = new User(1, "Tony", "Stark", "tony.stark@starkindustries.com", "CEO and Founder",
                "2008-05-02", "USA", "New York");
        User user2 = new User(2, "Steve", "Rogers", " steve.rogers@avengers.org", "Avenger",
                "1942-03-03", "UK", "London");
        List<User> userList = Arrays.asList(user1, user2);
        when(csvFileService.getUsersCreatedBetween(eq(startDate), eq(endDate))).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> response = userController.getUsersCreatedBetween(startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(CollectionUtils.isEmpty(response.getBody()));
        assertEquals(userList, response.getBody());
    }

    @Test
    public void testGetUsersCreatedBetween_Fail() {
        // Arrange
        LocalDate startDate = LocalDate.parse("2099-01-01");
        LocalDate endDate = LocalDate.parse("2099-12-31");
        List<User> userList = new ArrayList<>();
        when(csvFileService.getUsersCreatedBetween(eq(startDate), eq(endDate))).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> response = userController.getUsersCreatedBetween(startDate, endDate);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(CollectionUtils.isEmpty(response.getBody()));
    }

    @Test
    void testGetUsersByProfession_Success() {
        // Arrange
        String profession = "Developer";
        User user1 = new User(1, "Thor", "Odinson", "thor.odinson@asgard.com", "God of Thunder",
                "2011-08-27", "Asgard", "Asgard");
        User user2 = new User(2, "Bruce", "Banner", "bruce.banner@hulkmail.com", "Scientist",
                "2008-06-20", "USA", "Dayton");
        List<User> userList = Arrays.asList(user1, user2);
        when(csvFileService.getUsersByProfession(eq(profession))).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> response = userController.getUsersByProfession(profession);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(CollectionUtils.isEmpty(response.getBody()));
        assertEquals(userList, response.getBody());
    }

    @Test
    void testGetUsersByProfession_Fail() {
        // Arrange
        String profession = "Project Manager";
        List<User> userList = new ArrayList<>();
        when(csvFileService.getUsersByProfession(eq(profession))).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> response = userController.getUsersByProfession(profession);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(CollectionUtils.isEmpty(response.getBody()));
    }

    @Test
    void testAddUserToCSV_Success() {
        // Arrange
        User user = new User(1, "Natasha", "Romanoff", "natasha.romanoff@avengers.org", "Spy",
                "2023-05-01", "Russia", "Sokolovo");
        when(csvFileService.addUserToCSV(eq(user))).thenReturn(true);

        // Act
        ResponseEntity<String> response = userController.addUserToCSV(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAddUserToCSV_Fail() {
        // Arrange
        User user = new User(1, "Thanos", "Thanos", "thanos@titan.net", "Conqueror",
                "2023-05-01", "Titan", "Titan");
        when(csvFileService.addUserToCSV(eq(user))).thenReturn(false);

        // Act
        ResponseEntity<String> response = userController.addUserToCSV(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

}