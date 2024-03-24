package com.example.javadevelopertask.utilDto.dto;

import com.example.javadevelopertask.enums.UserRoles;

public record SignUpDto(String email, String password, UserRoles role) {
}
