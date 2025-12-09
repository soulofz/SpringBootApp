package by.tms.service;

import by.tms.exception.UsernameExistsException;
import by.tms.model.Security;
import by.tms.model.UserRegistrationDto;
import by.tms.repository.SecurityRepository;
import by.tms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean isUsernameUsed(String username) {
        return securityRepository.getSecurityByUsername(username).isPresent();
    }

    public Optional<Security> getSecurityById(int id){
        return securityRepository.getSecurityById(id);
    }
}
