package ir.mmdaminah.newtest.file.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BucketValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBucket {
    String message() default "Invalid bucket name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
