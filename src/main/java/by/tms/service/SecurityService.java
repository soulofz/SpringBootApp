package by.tms.service;


import by.tms.exception.UsernameExistsException;
import by.tms.model.UserRegistrationDto;
import by.tms.repository.SecurityRepository;
import by.tms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SecurityService {
    private UserRepository userRepository;
    private SecurityRepository securityRepository;

    public SecurityService(UserRepository userRepository, SecurityRepository securityRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    public boolean registration(UserRegistrationDto userRegistrationDto) throws UsernameExistsException {
        if (isUsernameUsed(userRegistrationDto.getUsername())) {
            throw new UsernameExistsException(userRegistrationDto.getUsername());
        }
        try {
            return securityRepository.registration(userRegistrationDto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean isUsernameUsed(String username) {
        return securityRepository.getSecurityByUsername(username).isPresent();
    }
}
