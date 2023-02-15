package com.spring.start.springProjekt.user;


import com.spring.start.springProjekt.amazonAWS.AmazonAWSFacade;
import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.utilities.AppdemoUtils;
import com.spring.start.springProjekt.validators.UserRegisterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Controller
class RegisterController {
    private final String accessLevel = "ROLE_USER";
    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);
    private final UserService userService;
    private final MessageSource messageSource;
    private final AmazonAWSFacade amazonAWSFacade;

    RegisterController(UserService userService, MessageSource messageSource, AmazonAWSFacade amazonAWSFacade) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.amazonAWSFacade = amazonAWSFacade;
    }

    @GetMapping("/register")
    String registerForm(Model model) {

        LOG.debug("**** INVOKED > registerForm()");

        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/adduser")
    String registerAction(@ModelAttribute("user") UserDTO user, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException, MessagingException {

        LOG.debug("**** INVOKED > registerAction()");

        String returnPage;

        UserDTO userExist = userService.findUserByEmail(user.getEmail());

        new UserRegisterValidator().validateEmailExist(userExist, result);
        new UserRegisterValidator().validate(user, result);

        if (result.hasErrors()) {
            LOG.error("**** INVOKED > registerAction(), adding a new user failed.");
            returnPage = "register";
        } else {
            user.setActivationCode(AppdemoUtils.randomStringGenerator());

//            amazonAWSFacade.sendEmailWithActivationLink(user, locale);
            userService.saveNewUser(user, accessLevel);
//            model.addAttribute("message", messageSource.getMessage("user.register.success.email", null, locale));
//            model.addAttribute("message2", messageSource.getMessage("user.register.success.email2", null, locale));

            model.addAttribute("message", messageSource.getMessage("user.register.success", null, locale));
            LOG.debug("**** INVOKED > registerAction(), adding a new user successfully.");
            returnPage = "afteredit";
        }

        return returnPage;
    }

    @PostMapping("/activatelink/{activationCode}")
    String activateAccount(@PathVariable("activationCode") String activationCode, Model model, Locale locale) {

        userService.updateUserActivation(1, activationCode);

        model.addAttribute("message", messageSource.getMessage("user.register.success", null, locale));

        LOG.debug("**** INVOKED > activateAccount(), account activation was successful.");

        return "index";
    }
}
