package vn.fu_ohayo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class AgeRangeValidator implements ConstraintValidator<AgeRange, Date> {

    private int minAge;
    private int maxAge;

    @Override
    public void initialize(AgeRange constraintAnnotation) {
        this.minAge = constraintAnnotation.min();
        this.maxAge = constraintAnnotation.max();
    }


    @Override
    public boolean isValid(Date dob, ConstraintValidatorContext context) {
        if (dob == null) return true; // @NotNull kiểm tra null

        LocalDate birthDate = dob.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate today = LocalDate.now();

        int age = Period.between(birthDate, today).getYears();

        return age >= minAge && age <= maxAge;
    }
}
