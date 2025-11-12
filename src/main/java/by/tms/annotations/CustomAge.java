package by.tms.annotations;

import by.tms.service.validation.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = {AgeValidator.class})
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAge {

    String message() default "The age should be between 18 and 120";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
