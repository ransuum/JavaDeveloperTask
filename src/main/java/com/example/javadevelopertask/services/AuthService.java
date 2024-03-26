package com.example.javadevelopertask.services;

import com.example.javadevelopertask.exception.InvalidJwtException;
import com.example.javadevelopertask.model.entity.Users;
import com.example.javadevelopertask.repo.UsersRepo;
import com.example.javadevelopertask.utilDto.dto.SignUpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = usersRepo.findByEmail(email);
        return user;
    }
    public UserDetails signUp(SignUpDto data) throws InvalidJwtException {
        if (usersRepo.findByEmail(data.email()) != null) {
            throw new InvalidJwtException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Users newUser = new Users(data.email(), encryptedPassword, data.role());
        return usersRepo.save(newUser);
    }
}