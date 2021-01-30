package com.spring.start.springProjekt.User;


import com.spring.start.springProjekt.emailSender.AmazonEmailSender;
import com.spring.start.springProjekt.utilities.AppdemoUtils;
import com.spring.start.springProjekt.validators.UserRegisterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.ws.rs.POST;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Controller
public class RegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private Environment environment;

    @GetMapping("/register")
    public String registerForm(Model model) {

        LOG.debug("**** WyWOŁANO > registerForm()");

        User u = new User();
        model.addAttribute("user", u);
        return "register";
    }


    @POST
    @RequestMapping(value = "/adduser")
    public String registerAction(User user, BindingResult result, Model model, Locale locale) throws UnsupportedEncodingException, MessagingException {

        LOG.debug("**** WyWOŁANO > registerAction()");

        String returnPage = null;

        User userExist = userService.findUserByEmail(user.getEmail());

        new UserRegisterValidator().validateEmailExist(userExist, result);
        new UserRegisterValidator().validate(user, result);

        if (result.hasErrors()) {

            LOG.error("**** WyWOŁANO > registerAction(), dodanie nowego użytkownika nie powiodło się.");

            returnPage = "register";
        } else {
            user.setActivationCode(AppdemoUtils.randomStringGenerator());
            String content = messageSource.getMessage("user.register.email", null, locale) +
                    environment.getProperty("amazon.EB.domain") + "/activatelink/"+user.getActivationCode();

            AmazonEmailSender sender = new AmazonEmailSender();
            sender.sendEmail(environment,user.getEmail(), messageSource.getMessage("user.register.email.title",null,locale), content);

            userService.saveUser(user);
            model.addAttribute("message", messageSource.getMessage("user.register.success.email", null, locale));

            LOG.debug("**** WyWOŁANO > registerAction(), dodanie nowego użytkownika powiodło się.");
            returnPage = "index";
        }

        return returnPage;
    }

    @POST
    @RequestMapping(value = "/activatelink/{activationCode}")
    public String activateAccount(@PathVariable("activationCode") String activationCode, Model model, Locale locale) {

        userService.updateUserActivation(1, activationCode);

        model.addAttribute("message", messageSource.getMessage("user.register.success", null, locale));

        LOG.debug("**** WyWOŁANO > activateAccount(), aktywacja konta powiodła się.");

        return "index";
    }
}
