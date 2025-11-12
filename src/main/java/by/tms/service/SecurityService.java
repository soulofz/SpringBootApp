package by.tms.service;


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

    public boolean registration(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());

        try {
            return userRepository.insertUser(user, userRegistrationDto.getPassword()) > 0;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean isUsernameUsed(String username) {
        return userRepository.getUserByUsername(username).isPresent();
    }
}
