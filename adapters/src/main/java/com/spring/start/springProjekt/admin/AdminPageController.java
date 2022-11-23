package com.spring.start.springProjekt.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@Secured(value = {"ROLE_ADMIN", "ROLE_SUPERADMIN"})
class AdminPageController {

    private final static Logger LOG = LoggerFactory.getLogger(AdminPageController.class);

    @GetMapping
    String openAdminMainPage() {

        LOG.debug("**** INVOKED > openAdminMainPage()");

        return "admin/admin";
    }
}
