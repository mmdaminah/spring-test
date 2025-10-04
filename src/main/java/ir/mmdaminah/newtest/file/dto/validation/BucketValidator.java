package ir.mmdaminah.newtest.file.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BucketValidator implements ConstraintValidator<ValidBucket, String> {

    private final UploadBucketNames bucketNames;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) return false;
        return bucketNames.getBucketNames().contains(s);
    }
}
