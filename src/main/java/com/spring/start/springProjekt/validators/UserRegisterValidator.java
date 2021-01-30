package com.spring.start.springProjekt.validators;

import com.spring.start.springProjekt.User.User;
import com.spring.start.springProjekt.constants.AppDemoConstants;
import com.spring.start.springProjekt.utilities.AppdemoUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserRegisterValidator implements Validator {
//dwie podstawowe metody
    @Override
    public boolean supports(Class<?> cls) {//jako argument przyjmuje klasę obiektu i sprawdza czy pasuje do tego co oczekujemy
        return User.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {//konkretny walidator
        //rzutowanie ogólnego obiektu na user
        User u = (User) obj;

        //ValidationUtils - wyrzuc coś na zewnątrz gdy pole jest puste
        ValidationUtils.rejectIfEmpty(errors, "name", "error.userName.empty");// - trzeci argument to pole sf errors
        // które skolei zawiera odwołanie do komunikatu w message.propertie
        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.userLastName.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.userEmail.empty");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.userPassword.empty");

        //w drugiej kolejnosci sprawdzamy regexy
        if (!u.getEmail().equals(null)) {
            boolean isMatch = AppdemoUtils.checkEmailOrPassword(AppDemoConstants.EMAIL_PATTERN, u.getEmail());
            if(!isMatch) {
                errors.rejectValue("email", "error.userEmailIsNotMatch");
            }
        }

        if (!u.getPassword().equals(null)) {
            boolean isMatch = AppdemoUtils.checkEmailOrPassword(AppDemoConstants.PASSWORD_PATTERN, u.getPassword());
            if(!isMatch) {
                errors.rejectValue("password", "error.userPasswordIsNotMatch");
            }
        }

    }

    public void validateEmailExist(User user, Errors errors) {//
        if (user != null) {
            errors.rejectValue("email", "error.userEmailExist");
        }
    }



}