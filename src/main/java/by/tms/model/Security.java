package by.tms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "security")
@Data
@NoArgsConstructor
public class Security {

    @Id
    @SequenceGenerator(name = "security_generator", sequenceName = "security_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "security_generator")
    private Integer id;

    @Column(name = "user_id", unique = true, nullable = false)
    private int userId;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
