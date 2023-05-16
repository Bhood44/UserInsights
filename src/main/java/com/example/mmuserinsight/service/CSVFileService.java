package com.example.mmuserinsight.service;

import com.example.mmuserinsight.dto.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CSVFileService {
    private static final String filePath = "src/main/resources/UserInformation.csv";

    private static final Logger logger = LoggerFactory.getLogger(CSVFileService.class);

    public User getUserById(Integer id) {
        List<User> userList = readUsersFromCSV(filePath);
        log.info("Retrieving user by id: " + id + " from " + filePath);
        return userList.stream().filter(user -> user.getId().equals(id))
                .findFirst().orElse(new User());
    }

    public List<User> getUsersCreatedBetween(LocalDate startDate, LocalDate endDate) {
        List<User> userList = readUsersFromCSV(filePath);
        log.info("Validating dates created within range of " + startDate + " - " + endDate);
        return userList.stream()
                .filter(user -> checkIfValidBetween(user.getDataCreated(), startDate, endDate))
                .collect(Collectors.toList());
    }

    public boolean checkIfValidBetween(String dateCreated, LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formattedDate = LocalDate.parse(dateCreated, dateTimeFormatter);
        return formattedDate.isAfter(startDate) && formattedDate.isBefore(endDate);
    }

    public List<User> getUsersByProfession(String profession) {
        List<User> userList = readUsersFromCSV(filePath);
        log.info("Retrieving users by profession: " + profession);
        return userList.stream()
                .filter(user -> user.getProfession().equalsIgnoreCase(profession))
                .collect(Collectors.toList());
    }

    /**
     * Gets max number among existing ids then increases by 1 for next
     * id then adds/writes a user to a CSV file.
     *
     * @param 'User' : The user object to be added.
     * @return boolean : True if the user is successfully added, false.
     * @throws RuntimeException : if an IOException occurs while writing to csv file.
     */
    public boolean addUserToCSV(User user) {
        try {
            List<User> userList = readUsersFromCSV(filePath);
            Integer newId = userList.stream().map(User::getId).max(Integer::compareTo).get() + 1;

            List<String> csvFormattedUser = Arrays.asList(
                    newId.toString(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getProfession(),
                    user.getDataCreated(),
                    user.getCountry(),
                    user.getCity());

            Writer writer = new FileWriter(filePath, true);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withRecordSeparator("\n"));
            csvPrinter.printRecord(csvFormattedUser);
            csvPrinter.flush();
            log.info("User added to CSV file for userId: " + newId);

            return true;
        } catch (IOException e) {
            log.error("Error occurred while adding new user to CSV file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads users from a CSV file and returns a list of existing Users.
     *
     * @param 'string' : filePath contains path to the CSV file to be read.
     * @return List<User> : A list of Users extracted from the CSV file.
     * @throws RuntimeException : if an IOException occurs while writing to csv file.
     */
    public List<User> readUsersFromCSV(String filePath) {
        try {
            CSVParser csvParser = CSVParser.parse(new File(filePath), Charset.defaultCharset(), CSVFormat.DEFAULT);
            List<User> users = new ArrayList<>();

            boolean skipHeader = true;

            for (CSVRecord csvRecord : csvParser) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                User user = mapToUser(csvRecord);
                users.add(user);
            }
            return users;

        } catch (IOException e) {
            log.error("Error occurred while reading from CSV file");
            throw new RuntimeException(e);
        }
    }

    private User mapToUser(CSVRecord csvRecord) {
        Integer id = Integer.valueOf(csvRecord.get(0));
        String firstName = csvRecord.get(1);
        String lastName = csvRecord.get(2);
        String email = csvRecord.get(3);
        String profession = csvRecord.get(4);
        String dateCreated = csvRecord.get(5);
        String country = csvRecord.get(6);
        String city = csvRecord.get(7);

        return new User(id, firstName, lastName, email, profession, dateCreated, country, city);
    }
}
