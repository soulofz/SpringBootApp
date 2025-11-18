package by.tms.service;


import by.tms.exception.UsernameExistsException;
import by.tms.model.User;
import by.tms.model.UserRegistrationDto;
import by.tms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityService {
    private UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean registration(UserRegistrationDto userRegistrationDto) throws UsernameExistsException {
        if (isUsernameUsed(userRegistrationDto.getUsername())) {
            throw new UsernameExistsException(userRegistrationDto.getUsername());
        }
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setAge(userRegistrationDto.getAge());
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        try {
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean isUsernameUsed(String username) {
        return userRepository.getUserByUsername(username).isPresent();
    }
}
