package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminAuthorizationService {

    private UserRepo userRepo;

    @Autowired
    public AdminAuthorizationService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public LoginResult loginAsAdmin(final String username, final String password) {
        try {
            String user = userRepo.login(username, password);
            return user.equals("admin") ? LoginResult.ADMIN : LoginResult.REGULAR;
        } catch (InvalidCredentials e){
            return LoginResult.FAILED;
        }
    }
}
