package by.tms.security;

import by.tms.exception.UsernameExistsException;
import by.tms.exception.WrongPasswordException;
import by.tms.model.Role;
import by.tms.model.Security;
import by.tms.model.User;
import by.tms.model.dto.AuthRequest;
import by.tms.model.dto.UserRegistrationDto;
import by.tms.repository.SecurityRepository;
import by.tms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityService(UserRepository userRepository, SecurityRepository securityRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(rollbackFor = {Exception.class},
            noRollbackFor = {UsernameExistsException.class},
            isolation = Isolation.READ_COMMITTED)

    public boolean registration(UserRegistrationDto userRegistrationDto) throws UsernameExistsException {
        log.info("Registering user: {}", userRegistrationDto.getUsername());
        if (isUsernameUsed(userRegistrationDto.getUsername())) {
            throw new UsernameExistsException(userRegistrationDto.getUsername());
        }
        try {
            User user = new User();
            user.setFirstName(userRegistrationDto.getFirstName());
            user.setLastName(userRegistrationDto.getLastName());
            user.setEmail(userRegistrationDto.getEmail());
            user.setAge(userRegistrationDto.getAge());
            user.setCreated(LocalDateTime.now());
            user.setUpdated(LocalDateTime.now());
            userRepository.save(user);

            Security security = new Security();
            security.setUsername(userRegistrationDto.getUsername());
            security.setPassword(userRegistrationDto.getPassword());
            security.setRole(Role.USER);
            securityRepository.save(security);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean isUsernameUsed(String username) {
        return securityRepository.existsByUsername(username);
    }

    public Optional<Security> getSecurityById(int id) {
        return securityRepository.findById(id);
    }

    public List<Security> getAllSecurityByRole(String role) {
        return securityRepository.customFindByRole(role);
    }

    public Optional<String> generateJwt(AuthRequest request) throws WrongPasswordException{
        Optional<Security> security = securityRepository.getByUsername(request.getUsername());
        if(security.isEmpty()){
            throw new UsernameNotFoundException(request.getUsername());
        }
        if(!bCryptPasswordEncoder.matches(request.getPassword(), security.get().getPassword())){
            throw new WrongPasswordException(request.getPassword());
        }
        return Optional.ofNullable(jwtUtils.generateToken(security.get().getUsername()));
    }

    public List<Security> getAllSecuritiesByRole(String role) {
        return securityRepository.customFindByRole(role);
    }
}
