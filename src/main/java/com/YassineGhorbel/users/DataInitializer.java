package com.YassineGhorbel.users;

import com.YassineGhorbel.users.entities.Role;
import com.YassineGhorbel.users.entities.User;
import com.YassineGhorbel.users.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final boolean bootstrapEnabled;
    private final String adminPassword;
    private final String userPassword;

    public DataInitializer(UserService userService,
                           @Value("${app.bootstrap.users.enabled:true}") boolean bootstrapEnabled,
                           @Value("${app.bootstrap.admin-password:123}") String adminPassword,
                           @Value("${app.bootstrap.user-password:123}") String userPassword) {
        this.userService = userService;
        this.bootstrapEnabled = bootstrapEnabled;
        this.adminPassword = adminPassword;
        this.userPassword = userPassword;
    }

    @Override
    public void run(String... args) {
        if (bootstrapEnabled) {
            ensureRole("ADMIN");
            ensureRole("USER");
            ensureUser("admin", adminPassword, List.of("ADMIN", "USER"));
            ensureUser("nadhem", userPassword, List.of("USER"));
            ensureUser("yassine", userPassword, List.of("USER"));
        }
    }

    private void ensureRole(String name) {
        if (userService.findRoleByName(name) == null) {
            userService.addRole(new Role(null, name));
        }
    }

    private void ensureUser(String username, String password, List<String> roles) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            user = userService.saveUser(new User(null, username, password, true, null, null));
        }
        for (String role : roles) {
            userService.addRoleToUser(username, role);
        }
    }
}