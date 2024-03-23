package com.ygstar.backend.sys.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String userMail;

    private String phoneNumber;

    private String password;

    private String confirmKey;

    private String emailCode;
}
