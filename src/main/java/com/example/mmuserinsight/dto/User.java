package com.example.mmuserinsight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    //id,firstname,lastname,email,profession,dateCreated,country,city
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String profession;

    private String dataCreated;

    private String country;

    private String city;
}