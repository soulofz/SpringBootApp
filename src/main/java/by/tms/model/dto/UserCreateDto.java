package by.tms.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Schema(description = "Это пользователь для создания в системе")
@Data
public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
