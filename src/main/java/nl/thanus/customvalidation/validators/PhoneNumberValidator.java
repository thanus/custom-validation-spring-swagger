package nl.thanus.customvalidation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private static final String REGEX = "[0-9+]+";
    private String countryCode;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        this.countryCode = constraintAnnotation.countryCode();
        validateParameter();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.startsWith(countryCode) && value.matches(REGEX);
    }

    private void validateParameter() {
        if (!countryCode.matches(REGEX)) {
            throw new IllegalArgumentException("The country code parameter must contain only numbers and a plus sign.");
        }
        if (countryCode.length() != 3) {
            throw new IllegalArgumentException("The length of the country code parameter must be 3.");
        }
        if (!countryCode.startsWith("+")) {
            throw new IllegalArgumentException("The country code must start with a plus sign.");
        }
    }
}
