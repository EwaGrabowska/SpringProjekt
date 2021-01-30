package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    @GetMapping(value = {"/", "/index"})
    public String showMainPage() {

        LOG.info("**** WYWOŁANO > showMainPage()");

        return "index";
    }
}
