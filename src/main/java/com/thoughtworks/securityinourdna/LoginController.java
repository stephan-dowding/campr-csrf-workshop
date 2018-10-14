package com.thoughtworks.securityinourdna;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

@Controller
public class LoginController {

    private final AdminAuthorizationService adminAuthorizationService;

    @Autowired
    public LoginController(AdminAuthorizationService adminAuthorizationService) {
        this.adminAuthorizationService = adminAuthorizationService;
    }

    @PostMapping("/login")
    public String createSession(@RequestParam(value = "username") String username,
                                @RequestParam(value = "password") String password,
                                HttpSession session, HttpServletResponse response) {

        final LoginResult loggedIn = adminAuthorizationService.loginAsAdmin(username, password);

        if (loggedIn == LoginResult.ADMIN) {
            final UserState userState = new UserState(true);
            session.setAttribute("userState", userState);
            return "redirect:/admin";
        }
        if (loggedIn == LoginResult.REGULAR) {
            final UserState userState = new UserState(false);
            session.setAttribute("userState", userState);
            return "redirect:/vendorhome.html";
        }
        return "Sorry! There is something wrong with your username and password combination.";
    }

    @GetMapping(value = "/logout")
    public String logout (final HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
