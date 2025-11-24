package by.tms.repository;

import by.tms.model.User;
import by.tms.model.UserCreateDto;
import by.tms.util.SqlList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepository {
    private final Connection connection;
    private final int ONE_LINE_FROM_DB = 1;

    @Autowired
    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public List<User> getAllUsers() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            return parseResultSetToUserList(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public Optional<User> getUserById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.GET_USER_BY_ID);
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

    public boolean addUser(UserCreateDto user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.INSERT_USER);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setInt(6, user.getAge());

            return preparedStatement.executeUpdate() == ONE_LINE_FROM_DB;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public User fillUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));
        user.setEmail(resultSet.getString("email"));
        user.setAge(resultSet.getInt("age"));
        user.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
        user.setUpdated(resultSet.getTimestamp("changed").toLocalDateTime());
        return user;
    }

    public boolean deleteUserById(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.DELETE_USER_BY_ID);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == ONE_LINE_FROM_DB;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateUser(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.UPDATE_USER_BY_ID);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setInt(4, user.getAge());
            preparedStatement.setInt(5,user.getId());
            return preparedStatement.executeUpdate() == ONE_LINE_FROM_DB;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
