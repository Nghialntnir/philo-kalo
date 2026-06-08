package com.nlnt.philokalo_server.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 *
 * @author nghia
 */
@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PatternValidator.class})
public @interface PatternConstraint {

    public String message() default "Invalid pattern";

    int min() default 8;

    int max() default Integer.MAX_VALUE;

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
