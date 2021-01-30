package com.spring.start.springProjekt.validators;

import com.spring.start.springProjekt.User.User;
import com.spring.start.springProjekt.constants.AppDemoConstants;
import com.spring.start.springProjekt.utilities.AppdemoUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ChangePasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return User.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        @SuppressWarnings("unused")
        User u = (User) obj;
        //STANDARTOWA ADNOTACJA MÓWIĄCA CZY HASŁO JEST PUSTE
        ValidationUtils.rejectIfEmpty(errors, "newPassword", "error.userPassword.empty");

    }

    public void checkPasswords(String newPass, Errors errors) {

        //sprawdzamy hasło czy jest zgodne z regex
        if (!newPass.equals(null)) {
            boolean isMatch = AppdemoUtils.checkEmailOrPassword(AppDemoConstants.PASSWORD_PATTERN, newPass);
            if(!isMatch) {
                errors.rejectValue("newPassword", "error.userPasswordIsNotMatch");
            }
        }
    }
}