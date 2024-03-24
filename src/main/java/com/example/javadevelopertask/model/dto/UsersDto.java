package com.example.javadevelopertask.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private UUID id;
    private String email;
    private String password;
}
