package com.ead.course.dto;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String cpf;
    private String imageUrl;
    private UserStatus userStatus;
    private UserType userType;
}
