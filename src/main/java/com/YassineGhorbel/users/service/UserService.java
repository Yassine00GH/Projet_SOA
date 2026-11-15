package com.YassineGhorbel.users.service;

import com.YassineGhorbel.users.entities.Role;
import com.YassineGhorbel.users.entities.User;
import com.YassineGhorbel.users.register.RegistationRequest;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    Role findRoleByName(String roleName);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
    List<User> findAllUsers();
    User registerUser(RegistationRequest request);
    void sendEmailUser(User u, String code);
    User validateToken(String code);
}

