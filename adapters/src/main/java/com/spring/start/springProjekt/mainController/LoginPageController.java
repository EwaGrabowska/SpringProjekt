package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
class LoginPageController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginPageController.class);
    private final MessageSource messageSource;

    LoginPageController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/login")
    String login(Model model, String error, Locale locale) {

        LOG.debug("**** INVOKED > login()");

        if (error != null) {
            model.addAttribute("error", messageSource.getMessage("error.login", null, locale));

            LOG.error("**** INVOKED > login() Failed to login: " + error);

        } else {
            LOG.debug("**** INVOKED > login() Login was successful.");
        }

        return "login";
    }

}