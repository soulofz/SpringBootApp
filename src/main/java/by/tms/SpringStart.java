package by.tms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "App for learn Spring",
                description = "We use this app to learn Java:)",
                version = "1.0.0",
                contact = @Contact(
                        name = "Zhan",
                        email = "zhanminskzhan@gmail.com",
                        url = "https://github.com/soulofz"
                )
        )
)
@SpringBootApplication
public class SpringStart {
    public static void main(String[] args) {
        SpringApplication.run(SpringStart.class, args);
    }
}

