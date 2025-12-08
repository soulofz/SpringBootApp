package by.tms.util;

public interface SqlList {
    String INSERT_USER = "INSERT INTO users(id, first_name, last_name, created, updated, email, age) VALUES(DEFAULT, ?, ?, ?, ?, ?, ?)";
    String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    String UPDATE_USER_BY_ID = "UPDATE users SET first_name= ?, last_name= ?, updated= NOW(), email= ?, age= ? WHERE id = ?";
    String SELECT_ALL_USERS = "SELECT * FROM users";
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    String INSERT_SECURITY = "INSERT INTO security(id, user_id, username, password, role) VALUES (DEFAULT, :user_id, :username, :password, :role)";
    String GET_SECURITY_BY_USERNAME = "SELECT * FROM security WHERE username = :username";
}
