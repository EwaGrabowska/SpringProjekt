package com.spring.start.springProjekt.configuration;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
class MyAutoConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAutoConfiguration.class);

    @PostConstruct
    public void printConfigurationMessage() {
        LOGGER.info("Configuration for My Lib is complete...");
    }
}
