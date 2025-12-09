package by.tms.repository;

import by.tms.model.User;
import by.tms.model.UserCreateDto;
import org.hibernate.Session;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final Session session;
    private final String HQL = "from users where id = :id";

    @Autowired
    public UserRepository(Session session) {
        this.session = session;
    }

    public List<User> getAllUsers() {
        return session.createQuery("from users", User.class).getResultList();
    }

    public Optional<User> getUserById(int id) {
        Query<User> query = session.createQuery(HQL,User.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.uniqueResult());
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
        session.createMutationQuery(HQL).setParameter("id", id).executeUpdate();
        session.getTransaction().commit();
    }

    public Optional<User> updateUser(User user) {
        Optional<User> userBeforeUpdate = Optional.ofNullable(session.createQuery(HQL,User.class).setParameter("id", user.getId()).uniqueResult());
        if (userBeforeUpdate.isPresent()) {
            session.getTransaction().begin();
            session.evict(userBeforeUpdate.get());
            MutationQuery query = session.createQuery("update users set firstName = :firstName, lastName = :lastName, age = :age, email = :email, created = :created, updated =:updated where id = :id");
            query.setParameter("firstName", user.getFirstName());
            query.setParameter("lastName", user.getLastName());
            query.setParameter("age", user.getAge());
            query.setParameter("email", user.getEmail());
            query.setParameter("created", user.getCreated());
            query.setParameter("updated", LocalDateTime.now());
            query.setParameter("id", user.getId());
            query.executeUpdate();

            User afterUpdate = session.createQuery(HQL,User.class).setParameter("id", user.getId()).uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(afterUpdate);
        }
        return Optional.empty();
    }
}
