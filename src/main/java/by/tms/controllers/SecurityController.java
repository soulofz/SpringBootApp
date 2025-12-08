package by.tms.controllers;


import by.tms.exception.UsernameExistsException;
import by.tms.model.UserRegistrationDto;
import by.tms.service.SecurityService;
import by.tms.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController {
    public SecurityService securityService;
    public UserService userService;

    public SecurityController(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatusCode> registration(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                                       BindingResult bindingResult) throws UsernameExistsException {
        if (!bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();

            for (ObjectError objectError : bindingResult.getAllErrors()) {
                System.out.println(objectError);
                errorMessages.add(objectError.getDefaultMessage());
            }
            throw new ValidationException(String.valueOf(errorMessages));
        }
        if (securityService.registration(userRegistrationDto)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}