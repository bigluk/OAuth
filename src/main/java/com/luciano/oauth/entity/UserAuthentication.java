package com.luciano.oauth.entity;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication {
  
    @Id
    private String username;
    private String password;
    private String fullname;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private boolean accountExpired;
    private boolean accountLocked;  
    private boolean credentialsExpired;
    private boolean disabled;
    private List<String> authorithies;


}