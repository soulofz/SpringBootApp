package by.tms.repository;

import by.tms.model.Role;
import by.tms.model.Security;
import by.tms.util.SqlList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
