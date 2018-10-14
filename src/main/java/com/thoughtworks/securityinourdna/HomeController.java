package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/vendor")
    public String vendor(final HttpSession session) {
        if (session.getAttribute("userState") != null) {
            return "vendor";
        }
        return "redirect:/";
    }
}
