package by.tms.repository;

import by.tms.model.Role;
import by.tms.model.Security;
import by.tms.model.UserRegistrationDto;
import by.tms.util.SqlList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;


@Repository
public class SecurityRepository {
    private final Connection connection;

    @Autowired
    public SecurityRepository(Connection connection) {
        this.connection = connection;
    }

    public Optional<Security> getSecurityByUsername(String username) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.GET_SECURITY_BY_USERNAME);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return parseResultSetToSecurity(resultSet);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Security> parseResultSetToSecurity(ResultSet resultSet) throws SQLException {
        if (resultSet.next()){
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

    public boolean registration (UserRegistrationDto userRegistrationDto) {
        int userId = 0;
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SqlList.INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,userRegistrationDto.getFirstName());
            preparedStatement.setString(2,userRegistrationDto.getLastName());
            preparedStatement.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(5,userRegistrationDto.getEmail());
            preparedStatement.setInt(6,userRegistrationDto.getAge());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    userId = resultSet.getInt(1);
                }
            }
            if (userId == 0){
                connection.rollback();
                return false;
            }
            PreparedStatement statement = connection.prepareStatement(SqlList.INSERT_SECURITY);
            statement.setInt(1,userId);
            statement.setString(2, userRegistrationDto.getUsername());
            statement.setString(3, userRegistrationDto.getPassword());
            statement.setString(4,Role.USER.toString());
            if (updatedRows >0){
                connection.commit();
                return true;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
            try{
                connection.rollback();
            }catch (SQLException ex){
                System.out.println(ex.getMessage());
            }
        }finally {
            try{
                connection.setAutoCommit(true);
            }catch (SQLException ex){
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
        return false;
    }
}
