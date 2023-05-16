package com.example.mmuserinsight.controller;

import com.example.mmuserinsight.dto.User;
import com.example.mmuserinsight.service.CSVFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    //- Endpoint to return a specific user (and all associated information)
    //- Endpoint to return a list of users created between a date range
    //- Endpoint to return a list of users based on a specific profession
    //- Custom Endpoint that you design on your own.

    @Autowired
    private CSVFileService csvFileService;

    private static final Logger logger = LoggerFactory.getLogger(CSVFileService.class);

    @GetMapping(value = "user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) throws IOException {
        User user = csvFileService.getUserById(id);
        return user.getId() != null ? ResponseEntity.ok().body(user) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "date-range")
    public ResponseEntity<List<User>> getUsersCreatedBetween(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                             @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<User> userList = csvFileService.getUsersCreatedBetween(startDate, endDate);
        return (!CollectionUtils.isEmpty(userList)) ? ResponseEntity.ok().body(userList) : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/profession")
    public ResponseEntity<List<User>> getUsersByProfession(@RequestParam("profession") String profession) {
        List<User> userList = csvFileService.getUsersByProfession(profession);
        return (!CollectionUtils.isEmpty(userList)) ? ResponseEntity.ok().body(userList) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> addUserToCSV(@RequestBody User user) {
        boolean success = csvFileService.addUserToCSV(user);
        return success ? ResponseEntity.created(URI.create("/users/user/" + user.getId())).build() : ResponseEntity.internalServerError().build();
    }
}
