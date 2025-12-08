package by.tms.repository;

import by.tms.model.*;
import by.tms.util.SqlList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;


@Repository
public class SecurityRepository {
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    public SecurityRepository(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    public Optional<Security> getSecurityByUsername(String username) {
        Query query = entityManager.createNativeQuery(SqlList.GET_SECURITY_BY_USERNAME, Security.class);
        query.setParameter("username", username);
        Object security = query.getSingleResultOrNull();
        return Optional.ofNullable((Security) security);
    }

    public Optional<Security> parseResultSetToSecurity(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Security security = new Security();
            security.setId(resultSet.getInt("id"));
            security.setUsername(resultSet.getString("username"));
            security.setPassword(resultSet.getString("password"));
            security.setUserId(resultSet.getInt("user_id"));
            security.setRole(Role.valueOf(resultSet.getString("role")));
            return Optional.of(security);
        }
        return Optional.empty();
    }

    public boolean registration(UserRegistrationDto dto) {
        entityManager.getTransaction().begin();
        User user = userRepository.addUser(new UserCreateDto(dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getAge()));
        if (user.getId() == null) {
            return false;
        }
        Security security = new Security();
        security.setUserId(user.getId());
        security.setUsername(dto.getUsername());
        security.setPassword(dto.getPassword());
        security.setRole(Role.USER);
        entityManager.persist(security);
        entityManager.getTransaction().commit();
        return true;
    }
}