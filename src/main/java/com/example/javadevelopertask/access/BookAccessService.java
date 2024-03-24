package com.example.javadevelopertask.access;

import com.example.javadevelopertask.enums.UserRoles;
import com.example.javadevelopertask.model.entity.Users;
import com.example.javadevelopertask.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BookAccessService {
    @Autowired
    private UsersRepo usersRepo;

    public boolean checkRole(UUID user_id, UserRoles roles){
        Users byId = usersRepo.findById(user_id).orElse(null);
        if (byId == null){
            return false;
        }
        return byId.getRole().getValue().equals(roles.getValue());
    }
}
