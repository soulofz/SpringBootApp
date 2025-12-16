package by.tms.service;

import by.tms.exception.ForbiddenException;
import by.tms.exception.UserNotFoundException;
import by.tms.model.Role;
import by.tms.model.Security;
import by.tms.model.User;
import by.tms.model.dto.UserCreateDto;
import by.tms.repository.SecurityRepository;
import by.tms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;

    public UserService(UserRepository userRepository, SecurityRepository securityRepository) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) throws ForbiddenException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Security> security = securityRepository.getByUsername(username);
        if (security.isPresent() && security.get().getUser().getId() == id || security.get().getRole().equals(Role.ADMIN)) {
            return userRepository.findById(id);
        } else {
            throw new ForbiddenException();
        }
    }

    public User addUser(UserCreateDto user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setAge(user.getAge());
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public boolean deleteUserById(int id) throws ForbiddenException {
        if (getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        Optional<User> user = getUserById(id);
        return user.isEmpty();
    }

    public Optional<User> updateUser(User user) throws ForbiddenException {
        Optional<User> userOptional = getUserById(user.getId());
        if (userOptional.isPresent()) {
            return Optional.of(userRepository.saveAndFlush(user));
        } else {
            throw new UserNotFoundException(user);
        }
    }

    //Сортировка
    public List<User> getSortedUsersByField(String field, String order) {
        if (order != null && !order.isBlank() && order.equalsIgnoreCase("desc")) {
            return userRepository.findAll(Sort.by(Sort.Direction.DESC, field));
        }
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    //Пагинация
    public Page<User> getAllUsersWithPagination(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<User> getInfoAboutMyself() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Security> security = securityRepository.getByUsername(username);
        if (security.isPresent()) {
            return userRepository.findById(security.get().getUser().getId());
        }
        return Optional.empty();
    }
}
