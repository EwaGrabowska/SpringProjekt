package com.spring.start.springProjekt.mainController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.GET;

@Controller
public class ErrorPageController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    private static final Logger LOG = LoggerFactory.getLogger(ErrorPageController.class);

    @GET
    @RequestMapping(value = "/error")
    public String showErrorPage() {

        LOG.error("Error page.");

        return "error";
    }


}