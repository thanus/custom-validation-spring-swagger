package nl.thanus.customvalidation.validators;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PhoneNumberValidatorTest {

    private static final String PHONE_NUMBER_SIZE_VIOLATION_MESSAGE = "size must be between 10 and 15";
    private static final String PHONE_NUMBER_VIOLATION_MESSAGE = "Invalid phone number";
    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void shouldValidatePhoneNumber() {
        final Number number = new Number();
        number.number = "+31234567890";

        Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void shouldValidateNumberWhenNull() {
        final Set<ConstraintViolation<Number>> violations = validator.validate(new Number());

        assertThat(violations.isEmpty()).isTrue();
    }

    @Test
    public void shouldFailWhenInvalidCountryCode() {
        final Number number = new Number();
        number.number = "+44234567890";

        final Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(PHONE_NUMBER_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenPhoneNumberIsTooShort() {
        final Number number = new Number();
        number.number = "+31123456";

        final Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(PHONE_NUMBER_SIZE_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenPhoneNumberIsTooLong() {
        final Number number = new Number();
        number.number = "+311234567890123";

        final Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(PHONE_NUMBER_SIZE_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenPhoneNumberContainsNoNumbers() {
        final Number number = new Number();
        number.number = "qwertyuiop";

        final Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(PHONE_NUMBER_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenPhoneNumberContainsNoNumbersAndIsTooShort() {
        final Number number = new Number();
        number.number = "qwertyuio";

        final Set<ConstraintViolation<Number>> violations = validator.validate(number);

        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(PHONE_NUMBER_SIZE_VIOLATION_MESSAGE, PHONE_NUMBER_VIOLATION_MESSAGE);
    }

    @Test
    public void shouldFailWhenCountryCodeArgumentContainsCharacters() {
        assertThatThrownBy(() -> validator.validate(new InvalidNumberWithCharacters()))
                .hasCause(new IllegalArgumentException("The country code parameter must contain only numbers and a plus sign."));
    }

    @Test
    public void shouldFailWhenCountryCodeArgumentIsTooLong() {
        assertThatThrownBy(() -> validator.validate(new InvalidLongNumber()))
                .hasCause(new IllegalArgumentException("The length of the country code parameter must be 3."));
    }

    @Test
    public void shouldFailWhenCountryCodeIsNotStartingWithPlusSign() {
        assertThatThrownBy(() -> validator.validate(new InvalidNumberWithoutPlusSign()))
                .hasCause(new IllegalArgumentException("The country code must start with a plus sign."));
    }

    static class Number {
        @PhoneNumber(countryCode = "+31")
        String number;
    }

    static class InvalidNumberWithCharacters {
        @PhoneNumber(countryCode = "+qwe")
        String number;
    }

    static class InvalidLongNumber {
        @PhoneNumber(countryCode = "+311")
        String number;
    }

    static class InvalidNumberWithoutPlusSign {
        @PhoneNumber(countryCode = "311")
        String number;
    }
}
