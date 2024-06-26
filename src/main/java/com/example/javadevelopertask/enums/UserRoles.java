package com.example.javadevelopertask.enums;

public enum UserRoles {
    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }
}
