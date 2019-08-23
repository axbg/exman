package com.web.poc1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/menu")
                .setViewName("forward:/");
        registry.addViewController("/grid")
                .setViewName("forward:/");
        registry.addViewController("/users")
                .setViewName("forward:/");
    }
}