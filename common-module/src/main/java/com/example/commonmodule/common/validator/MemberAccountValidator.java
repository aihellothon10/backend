package com.example.commonmodule.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MemberAccountValidator implements ConstraintValidator<MemberAccount, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//        Pattern pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
//        Matcher matcher = pattern.matcher(value);
//        return matcher.matches();
        return true;
    }

}
