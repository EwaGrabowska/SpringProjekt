package com.spring.start.springProjekt.validators;

import com.spring.start.springProjekt.constants.AppDemoConstants;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.utilities.AppdemoUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ChangePasswordValidator implements Validator {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean supports(Class<?> cls) {
        return UserDTO.class.equals(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        @SuppressWarnings("unused")
        UserDTO u = (UserDTO) obj;
        ValidationUtils.rejectIfEmpty(errors, "oldPasswordCheck", "error.userPassword.empty");
        ValidationUtils.rejectIfEmpty(errors, "newPassword", "error.userPassword.empty");
        ValidationUtils.rejectIfEmpty(errors, "passwordConfirm", "error.userPassword.empty");

        if (!u.getNewPassword().equals(null)) {
            boolean isMatch = AppdemoUtils.checkEmailOrPassword(AppDemoConstants.PASSWORD_PATTERN, u.getNewPassword());
            if(!isMatch) {
                errors.rejectValue("newPassword", "error.userPasswordIsNotMatch");
            }
        }
    }
    public void oldPasswordCheck(String password, String passwordEncoded, Errors errors){
        boolean passwordMatch = bCryptPasswordEncoder.matches(password, passwordEncoded);
        if(!passwordMatch) {
            errors.rejectValue("oldPasswordCheck", "error.oldPasswordNotMatch");
        }
    }

    public void confirmPasswordCheck(String newPassword, String passwordConfirm, Errors errors){
        boolean passwordMatch = newPassword.equals(passwordConfirm);
        if(!passwordMatch) {
            errors.rejectValue("passwordConfirm", "error.passwordConfirmNotMatch");
        }
    }
}