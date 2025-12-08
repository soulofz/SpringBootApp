package by.tms.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity(name = "users")
@Data
@Component
public class User {

    @Id
    @SequenceGenerator(name = "user_generator", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_generator")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private int age;
    private LocalDateTime created;
    private LocalDateTime updated;
}
