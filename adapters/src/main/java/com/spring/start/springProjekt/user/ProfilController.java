package com.spring.start.springProjekt.user;

import com.spring.start.springProjekt.user.DTO.UserDTO;
import com.spring.start.springProjekt.utilities.UserUtilities;
import com.spring.start.springProjekt.validators.ChangePasswordValidator;
import com.spring.start.springProjekt.validators.EditUserProfileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@Secured(value = {"ROLE_ADMIN", "ROLE_USER", "ROLE_SUPERADMIN"})
class ProfilController {
    private static final Logger LOG = LoggerFactory.getLogger(ProfilController.class);
    private final UserService userService;
    private final MessageSource messageSource;

    ProfilController(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @GetMapping("/profil")
    String showUserProfilePage(Model model) {

        LOG.debug("**** INVOKED > showUserProfilePage()");

        var username = UserUtilities.getLoggedUser();
        UserDTO user = userService.findUserByEmail(username);
        var roleNumber = user.getRoles().iterator().next().getId();
        user.setRoleNumber(roleNumber);
        model.addAttribute("user", user);
        return "profil";
    }

    @GetMapping("/editpassword")
    String editUserPassword(Model model) {

        LOG.debug("**** INVOKED > editUserPassword()");

        var username = UserUtilities.getLoggedUser();
        UserDTO user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editpassword";
    }

    @PostMapping("/updatepass")
    String changeUSerPassword(@ModelAttribute("user") UserDTO user, BindingResult result, Model model, Locale locale, HttpServletRequest request) {

        LOG.debug("**** INVOKED > editUserPassword()");

        UserDTO loggedUser = userService.findUserByEmail(user.getEmail());
        String returnPage;
        ChangePasswordValidator validator = new ChangePasswordValidator();
        validator.validate(user, result);
        validator.oldPasswordCheck(user.getOldPasswordCheck(), loggedUser.getPassword(), result);
        validator.confirmPasswordCheck(user.getNewPassword(), user.getPasswordConfirm(), result);

        if (result.hasErrors()) {
            LOG.error("**** INVOKED > editUserPassword(), password change failed.");
            result.getAllErrors().stream().forEach(System.out::println);
            returnPage = "editpassword";
        } else {
            userService.updateUserPassword(user.getNewPassword(), user.getEmail());
            LOG.debug("**** INVOKED > editUserPassword(), password change was successful.");
            model.addAttribute("message", messageSource.getMessage("profilEdit.success", null, locale));
            model.addAttribute("message2", messageSource.getMessage("profilEdit.success2", null, locale));
            request.getSession(true).invalidate();
            returnPage = "afteredit";

        }

        return returnPage;
    }

    @GetMapping("/editprofil")
    String changeUserData(Model model) {

        LOG.debug("**** INVOKED > changeUserData()");

        var username = UserUtilities.getLoggedUser();
        UserDTO user = userService.findUserByEmail(username);
        model.addAttribute("user", user);
        return "editprofil";
    }

    @PostMapping("/updateprofil")
    String changeUserDataAction(@ModelAttribute("user") UserDTO user, BindingResult result, Model model, Locale locale, HttpServletRequest request) {

        LOG.debug("**** INVOKED > changeUserDataAction() for user " + user.getName() + " " + user.getLastName());

        String returnPage;
        new EditUserProfileValidator().validate(user, result);
        if (result.hasErrors()) {

            LOG.error("**** INVOKED > changeUserDataAction(), date change failed.");

            returnPage = "editprofil";
        } else {
            userService.updateUserProfile(user.getName(), user.getLastName(), user.getEmail(), user.getId());
            model.addAttribute("message", messageSource.getMessage("profilEdit.success", null, locale));
            model.addAttribute("message2", messageSource.getMessage("profilEdit.success2", null, locale));

            LOG.debug("**** INVOKED > changeUserDataAction(), date change was successful..");

            request.getSession(true).invalidate();
            returnPage = "afteredit";
        }
        return returnPage;
    }

    @GetMapping("/delete")
    String deleteAccount(Model model, Locale locale, HttpServletRequest request) throws ServletException {
        String loggedUser = UserUtilities.getLoggedUser();
        int id = userService.deleteUser(loggedUser);

        LOG.debug("[INVOKED >>> ProfilController.deleteUser > user id: " + id);

        model.addAttribute("message", messageSource.getMessage("profilDelete.success", null, locale));
        request.getSession(true).invalidate();
        return "afteredit";
    }



}