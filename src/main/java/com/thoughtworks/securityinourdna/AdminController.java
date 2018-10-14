package com.thoughtworks.securityinourdna;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.sql.SQLException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepo userRepo;

    @Autowired
    public AdminController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String adminHome(Model model, HttpSession session) {
        if (loggedInAsAdmin(session)) {
            String csrfToken = generateCSRFToken();
            //I'm sure we need to remember this in session for some reason.
            session.setAttribute("csrfToken", csrfToken);
            model.addAttribute("csrfToken", csrfToken);
            return "admin";
        }

        throw new UnauthorizedResponse();
    }

    @PostMapping("/deleteVendor")
    public String deleteVendor(HttpSession session,
                               RedirectAttributes redirAttrs,
                               @RequestParam(value = "vendor") String vendor,
                               // TODO: Do something with this... but first, features!
                               @RequestParam(value = "csrfToken") String csrfToken
                               ) throws SQLException {
        if (loggedInAsAdmin(session)) {
            userRepo.delete(vendor);
            redirAttrs.addFlashAttribute("deletedVendor", vendor);
            return "redirect:/admin";
        }

        throw new UnauthorizedResponse();
    }



    private String generateCSRFToken() {
        SecureRandom entropy = new SecureRandom();

        byte secureBytes[] = new byte[20];
        entropy.nextBytes(secureBytes);

        return Base64.encodeBase64String(secureBytes);
    }

    private boolean loggedInAsAdmin(HttpSession session) {
        final UserState userState = (UserState) session.getAttribute("userState");
        return userState != null && userState.isAdminLoggedIn();
    }
}
