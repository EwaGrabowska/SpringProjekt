package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class LoginPageController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginPageController.class);

    @Autowired
    MessageSource messageSource;

    @GetMapping("/login")
    public String login(Model model, String error, Locale locale) {

        LOG.debug("**** WyWOŁANO > login()");

        if (error != null) {
            model.addAttribute("error", messageSource.getMessage("error.logowanie", null, locale));

            LOG.error("Nie udało się zalogować: " + error);

        }

        LOG.debug("Logowanie przebiegło pomyślnie.");

        return "login";
    }

}