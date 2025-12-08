package by.tms.repository;

import by.tms.model.User;
import by.tms.model.UserCreateDto;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final Session session;

    @Autowired
    public UserRepository(Session session) {
        this.session = session;
    }

    public List<User> getAllUsers() {
        return session.createQuery("from users", User.class).getResultList();
    }

    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(session.find(User.class, id));
    }

   public User addUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setAge(userCreateDto.getAge());
        user.setCreated(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        session.getTransaction().begin();
        session.persist(user);
        session.getTransaction().commit();
        return user;
   }

    public void deleteUserById(int id) {
        session.getTransaction().begin();
        session.remove(session.find(User.class, id));
        session.getTransaction().commit();
    }

    public Optional<User> updateUser(User user) {
        Optional<User> userFromDB = getUserById(user.getId());
        if (userFromDB.isPresent()) {
            session.getTransaction().begin();
            user.setCreated(userFromDB.get().getCreated());
            user.setUpdated(LocalDateTime.now());
            Optional<User> updatedUser = Optional.ofNullable(session.merge(user));
            session.getTransaction().commit();
            return updatedUser;
        }
        return Optional.empty();
    }
}
