package com.example.backend.user;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer uid;
    public String username;
    public String password;
    private String name;
    private String gender;
    private Date birthDate;
    private String description;
    private int role;
}
