package vn.fu_ohayo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeRangeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeRange {

    String message() default "Age must be between {min} and {max}";
    int min() default 0;
    int max() default 150;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
