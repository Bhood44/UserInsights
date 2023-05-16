package com.example.mmuserinsight.service;

import com.example.mmuserinsight.dto.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVFileServiceTest {
    private CSVFileService csvFileService;

    @BeforeEach
    void setUp() {
        csvFileService = new CSVFileService();
    }

    @Test
    public void testGetUserById_Success() {
        User user = csvFileService.getUserById(100);
        assertNotNull(user);
        assertEquals(100, user.getId().intValue());
        assertEquals("Andree", user.getFirstName());
        assertEquals("Flita", user.getLastName());
    }

    @Test
    public void testGetUserById_Fail() {
        User user = csvFileService.getUserById(10);
        assertEquals(new User(), user);
    }

    @Test
    public void testGetUsersCreatedBetween_Success() {
        LocalDate startDate = LocalDate.parse("2020-01-01");
        LocalDate endDate = LocalDate.parse("2020-02-01");
        List<User> userList = csvFileService.getUsersCreatedBetween(startDate, endDate);
        Assertions.assertNotNull(userList);
        System.out.println(userList.size());
        Assertions.assertFalse(userList.isEmpty());
    }

    @Test
    public void testGetUsersCreatedBetween_Fail() {
        LocalDate startDate = LocalDate.parse("2099-01-01");
        LocalDate endDate = LocalDate.parse("2099-12-31");
        List<User> userList = csvFileService.getUsersCreatedBetween(startDate, endDate);
        System.out.println(userList);
        Assertions.assertTrue(userList.isEmpty());
    }

    @Test
    public void testGetUsersByProfession_Success() {
        String profession = "Developer";
        List<User> userList = csvFileService.getUsersByProfession(profession);
        Assertions.assertNotNull(userList);
        Assertions.assertFalse(userList.isEmpty());
    }

    @Test
    public void testGetUsersByProfession_Fail() {
        String profession = "Quant Dev";
        List<User> userList = csvFileService.getUsersByProfession(profession);
        Assertions.assertTrue(userList.isEmpty());
    }

    @Test
    public void testAddUserToCSV_Success() {
        User user = new User(4, "John", "117", "masterchief.117@unsc.com", "spartan",
                "2023-05-01", "USA", "New York");
        boolean success = csvFileService.addUserToCSV(user);
        Assertions.assertTrue(success);
    }
}