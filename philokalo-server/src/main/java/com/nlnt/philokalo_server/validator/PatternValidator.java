package com.nlnt.philokalo_server.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 * @author nghia
 */
public class PatternValidator implements ConstraintValidator<PatternConstraint, String> {

    private int min;
    private int max;
    
    @Override
    public void initialize(PatternConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        if (t == null || t.isBlank()) {
            return true;
        }
        if (t.length() < min || t.length() > max) {
            return false;
        }
        return t.matches("^[a-zA-Z0-9@.]*$");
    }

}
