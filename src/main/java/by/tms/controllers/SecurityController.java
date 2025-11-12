package by.tms.controllers;


import by.tms.model.User;
import by.tms.model.UserRegistrationDto;
import by.tms.service.SecurityService;
import by.tms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/security")
public class SecurityController {
    public SecurityService securityService;
    public UserService userService;

    public SecurityController(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            List<String> errMessages = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                System.out.println(objectError);
                errMessages.add(objectError.getDefaultMessage());
            }
            model.addAttribute("errors", errMessages);
            return "error";
        }
        Boolean result = securityService.registration(userRegistrationDto);
        if (result) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("usersKey", users );
            return "users";
        }
        return "error";
    }
}
