package by.tms.service;

import by.tms.exception.UserNotFoundException;
import by.tms.model.User;
import by.tms.model.UserCreateDto;
import by.tms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
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

    public boolean deleteUserById(int id) {
        if (getUserById(id).isEmpty()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        Optional<User> user = getUserById(id);
        return user.isEmpty();
    }

    public Optional<User> updateUser(User user) {
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
}
