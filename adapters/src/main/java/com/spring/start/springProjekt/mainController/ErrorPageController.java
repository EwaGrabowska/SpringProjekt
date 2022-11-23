package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/error")
class ErrorPageController implements ErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorPageController.class);

    @GetMapping
    String showErrorPage() {

        LOG.error("Error page.");

        return "error";
    }


}