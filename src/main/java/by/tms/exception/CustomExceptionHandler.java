package by.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UsernameExistsException.class)
    public ModelAndView usernameExistsException(UsernameExistsException e) {
        System.out.println("Username exists: " + e.getUsername());
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("errors", "Username " + e.getUsername() + " already exists");
        return modelAndView;
    }

    @ExceptionHandler(SQLException.class)
    public ModelAndView sqlException(SQLException e) {
        System.out.println("SQL exception: " + e.getMessage());
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        modelAndView.addObject("errors", "SQL exception: " + e.getMessage());
        return modelAndView;
    }
}