package com.lmsuniversity.security.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {
	String message() default "Password must contain at least one uppercase letter, one lowercase letter, and one digit";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
