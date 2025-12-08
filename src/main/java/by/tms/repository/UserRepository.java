package by.tms.repository;

import by.tms.model.User;
import by.tms.model.UserCreateDto;
import by.tms.util.SqlList;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final EntityManager entityManager;

    @Autowired
    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> getAllUsers() {
        return entityManager.createQuery("from users", User.class).getResultList();
    }

    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

   public User addUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setAge(userCreateDto.getAge());
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        return user;
   }

    public void deleteUserById(int id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(User.class, id));
        entityManager.getTransaction().commit();
    }

    public Optional<User> updateUser(User user) {
        Optional<User> userFromDB = getUserById(user.getId());
        if (userFromDB.isPresent()) {
            entityManager.getTransaction().begin();
            user.setCreated(userFromDB.get().getCreated());
            user.setUpdated(LocalDateTime.now());
            Optional<User> updatedUser = Optional.ofNullable(entityManager.merge(user));
            entityManager.getTransaction().commit();
            return updatedUser;
        }
        return Optional.empty();
    }
}
