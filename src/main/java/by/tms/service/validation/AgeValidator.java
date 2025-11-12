package by.tms.service.validation;

import by.tms.annotations.CustomAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<CustomAge, Integer> {
    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        return age != null && age >= 18 && age <= 120;
    }
}
