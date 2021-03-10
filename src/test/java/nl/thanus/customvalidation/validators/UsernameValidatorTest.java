package nl.thanus.customvalidation.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameValidatorTest {

    private static final String USERNAME_VIOLATION_MESSAGE = "Invalid username";
    private static Validator validator;

    @BeforeEach
    public void setUp() {
        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void shouldValidateUsername() {
        final User user = new User();
        user.username = "SPRING-123";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void shouldValidateUsernameWhenNull() {
        final Set<ConstraintViolation<User>> violations = validator.validate(new User());

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void shouldFailWhenUsernameFirstPartIsTooShort() {
        final User user = new User();
        user.username = "SPRIN-123";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenUsernameFirstPartIsTooLong() {
        final User user = new User();
        user.username = "SPRINGB-123";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenUsernameFirstPartContainsNumbers() {
        final User user = new User();
        user.username = "123-123";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenUsernameSecondPartIsTooLong() {
        final User user = new User();
        user.username = "SPRING-1234";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenUsernameSecondPartIsTooShort() {
        final User user = new User();
        user.username = "SPRING-1234";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenUsernameSecondPartContainsCharacters() {
        final User user = new User();
        user.username = "SPRING-boot";

        final Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(USERNAME_VIOLATION_MESSAGE);
    }

    static class User {
        @Username
        String username;
    }
}
