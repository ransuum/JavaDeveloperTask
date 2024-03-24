package com.example.javadevelopertask.services;

import com.example.javadevelopertask.enums.UserRoles;
import com.example.javadevelopertask.exception.InvalidJwtException;
import com.example.javadevelopertask.model.entity.Users;
import com.example.javadevelopertask.repo.UsersRepo;
import com.example.javadevelopertask.utilDto.dto.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {
    @Mock
    private UsersRepo usersRepo;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        String email = "test@example.com";
        Users mockUser = new Users(email, "encryptedPassword", UserRoles.USER);
        when(usersRepo.findByEmail(email)).thenReturn(mockUser);

        UserDetails userDetails = authService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void signUp() {
        SignUpDto signUpDto = new SignUpDto("newuser@example.com", "password", UserRoles.USER);
        when(usersRepo.findByEmail(signUpDto.email())).thenReturn(null);
        when(usersRepo.save(any(Users.class))).thenReturn(new Users(signUpDto.email(), "encryptedPassword", signUpDto.role()));

        UserDetails userDetails = authService.signUp(signUpDto);

        assertNotNull(userDetails);
        assertEquals(signUpDto.email(), userDetails.getUsername());
    }

    @Test
    public void testSignUpUserAlreadyExists() {
        SignUpDto signUpDto = new SignUpDto("existinguser@example.com", "password", UserRoles.USER);
        when(usersRepo.findByEmail(signUpDto.email())).thenReturn(new Users(signUpDto.email(), "encryptedPassword", signUpDto.role()));

        assertThrows(InvalidJwtException.class, () -> authService.signUp(signUpDto));
    }
}