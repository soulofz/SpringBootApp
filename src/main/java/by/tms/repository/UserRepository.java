package by.tms.repository;


import by.tms.model.User;
import by.tms.model.UserRegistrationDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Repository
public class UserRepository {
    private static final String INSERT_USER = "INSERT INTO users(id,username,password,first_name,last_name,created,updated,email,age) VALUES(DEFAULT,?,?,?,?,?,?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_USER_BY_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";

    private Connection connection;
    private final int ONE_LINE_FROM_DB = 1;

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        Properties props = new Properties();
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                props.getProperty("db_url"),
                props.getProperty("db_login"),
                props.getProperty("db_password"));
    }

    @PostConstruct
    public void init() throws SQLException, ClassNotFoundException {
        connection = createConnection();
    }

    public User fillUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        user.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        user.setUpdated(resultSet.getTimestamp("changed").toLocalDateTime());
        return user;
    }

    public List<User> getAllUsers() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            return parseResultSetToUserList(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public Optional<User> getUserById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return parseResultSetToUser(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public List<User> parseResultSetToUserList(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            userList.add(fillUser(resultSet));
        }
        return userList;
    }

    public Optional<User> parseResultSetToUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(fillUser(resultSet));
        }
        return Optional.empty();
    }

    public Optional<User> getUserByUsername(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_USERNAME_SQL);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return parseResultSetToUser(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public boolean deleteUserById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == ONE_LINE_FROM_DB;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean addUser(UserRegistrationDto user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(8, user.getAge());
            return preparedStatement.executeUpdate() == ONE_LINE_FROM_DB;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}

