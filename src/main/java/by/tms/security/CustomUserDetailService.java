package by.tms.security;

import by.tms.model.Security;
import by.tms.repository.SecurityRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private SecurityRepository securityRepository;

    public CustomUserDetailService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Security> securityOptional = securityRepository.getByUsername(username);
        if (securityOptional.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        Security security = securityOptional.get();
        return User
                .withUsername(security.getUsername())
                .password(security.getPassword())
                .roles(security.getRole().name())
                .build();
    }
}
