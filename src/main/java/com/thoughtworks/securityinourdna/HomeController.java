package com.thoughtworks.securityinourdna;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.SQLException;

@Controller
public class HomeController {

    private final UserRepo userRepo;


    @Autowired
    public HomeController(UserRepo userRepo, AdminAuthorizationService adminAuthorizationService) {
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public String home(Model model) throws SQLException {
        model.addAttribute("users", userRepo.allUsers());
        return "home";
    }
}
