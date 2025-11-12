package by.tms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
    Environment environment;

    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DatabaseConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(environment.getProperty("database_url"),
                    environment.getProperty("database_login"),
                    environment.getProperty("database_password"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
