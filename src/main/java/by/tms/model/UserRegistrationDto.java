package by.tms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDto {
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    private String password;
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
