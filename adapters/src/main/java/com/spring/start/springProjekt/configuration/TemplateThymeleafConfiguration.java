package com.spring.start.springProjekt.configuration;

import org.apache.commons.codec.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
class TemplateThymeleafConfiguration {

    @Bean
    ClassLoaderTemplateResolver webPageTemplateResolver() {
        ClassLoaderTemplateResolver webPageTemplateResolver = new ClassLoaderTemplateResolver();
        webPageTemplateResolver.setPrefix("templates/");
        webPageTemplateResolver.setSuffix(".html");
        webPageTemplateResolver.setTemplateMode("HTML5");
        webPageTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        webPageTemplateResolver.setOrder(1);
        return webPageTemplateResolver;
    }
}
