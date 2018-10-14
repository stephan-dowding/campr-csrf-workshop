package com.thoughtworks.securityinourdna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

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
                                HttpSession session,
                                RedirectAttributes redirAttrs) {

        final LoginResult loggedIn = adminAuthorizationService.loginAsAdmin(username, password);

        if (loggedIn == LoginResult.ADMIN) {
            final UserState userState = new UserState(true);
            session.setAttribute("userState", userState);
            return "redirect:/admin";
        }
        if (loggedIn == LoginResult.REGULAR) {
            final UserState userState = new UserState(false);
            session.setAttribute("userState", userState);
            return "redirect:/vendor";
        }

        redirAttrs.addFlashAttribute("error", "Sorry! There is something wrong with your username and password combination.");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout (final HttpSession session, RedirectAttributes redirAttrs) {
        session.invalidate();
        redirAttrs.addFlashAttribute("error", "You have logged out.");

        return "redirect:/";
    }
}
