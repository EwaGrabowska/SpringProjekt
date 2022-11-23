package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class MainPageController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    MainPageController() {
    }

    @GetMapping(value = {"/", "/index"})
    String showMainPage() {

        LOG.info("**** INVOKED > showMainPage()");

        return "index";
    }
}
