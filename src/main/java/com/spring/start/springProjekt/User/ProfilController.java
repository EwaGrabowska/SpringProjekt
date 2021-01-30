package com.spring.start.springProjekt.User;

import com.spring.start.springProjekt.utilities.UserUtilities;
import com.spring.start.springProjekt.validators.ChangePasswordValidator;
import com.spring.start.springProjekt.validators.EditUserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.Locale;

@Controller
public class ProfilController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @GET
    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @RequestMapping(value = "/profil")
    public String showUserProfilePage(Model model) {

        LOG.debug("**** WyWOŁANO > showUserProfilePage()");

        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);
        int nrRoli = user.getRoles().iterator().next().getId();
        user.setNrRoli(nrRoli);
        model.addAttribute("user", user);
        return "profil";
    }


    @GET
    @RequestMapping(value = "/editpassword")
    public String editUserPassword(Model model) {

        LOG.debug("**** WyWOŁANO > editUserPassword()");

        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editpassword";
    }

    @POST
    @RequestMapping(value = "/updatepass")
    public String changeUSerPassword(User user, BindingResult result, Model model, Locale locale) {

        LOG.debug("**** WyWOŁANO > editUserPassword()");

        String returnPage = null;
        new ChangePasswordValidator().validate(user, result);
        new ChangePasswordValidator().checkPasswords(user.getNewPassword(), result);
        if (result.hasErrors()) {

            LOG.error("**** WyWOŁANO > editUserPassword(), zmiana hasła nie powiodła się.");

            returnPage = "editpassword";
        } else {
            userService.updateUserPassword(user.getNewPassword(), user.getEmail());

            LOG.debug("**** WyWOŁANO > editUserPassword(). Zmiana hasła powiodła się.");

            returnPage = "editpassword";

        }

        return returnPage;
    }

    @GET
    @RequestMapping(value = "/editprofil")
    public String changeUserData(Model model) {

        LOG.debug("**** WyWOŁANO > changeUserData()");

        String username = UserUtilities.getLoggedUser();
        User user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editprofil";
    }

    @POST
    @RequestMapping(value = "/updateprofil")
    public String changeUserDataAction(User user, BindingResult result, Model model, Locale locale) {

        LOG.debug("**** WyWOŁANO > changeUserDataAction() dla użytkownika "+user.getName()+" "+user.getLastName());

        String returnPage = null;
        new EditUserProfileValidator().validate(user, result);
        if (result.hasErrors()) {

            LOG.error("**** WyWOŁANO > changeUserDataAction(), zmiana danych nie powiodła się.");

            returnPage = "editprofil";
        } else {
            userService.updateUserProfile(user.getName(), user.getLastName(), user.getEmail(), user.getId());
            model.addAttribute("message", messageSource.getMessage("profilEdit.success", null, locale));

            LOG.error("**** WyWOŁANO > changeUserDataAction(), zmiana danych powiodła się.");

            returnPage = "afteredit";
        }
        return returnPage;
    }

}