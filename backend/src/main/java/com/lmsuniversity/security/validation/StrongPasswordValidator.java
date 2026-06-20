package com.lmsuniversity.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return true;
		}
		boolean hasUppercase = value.chars().anyMatch(Character::isUpperCase);
		boolean hasLowercase = value.chars().anyMatch(Character::isLowerCase);
		boolean hasDigit = value.chars().anyMatch(Character::isDigit);
		return hasUppercase && hasLowercase && hasDigit;
	}
}
