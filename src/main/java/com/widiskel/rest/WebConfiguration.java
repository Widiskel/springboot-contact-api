package com.widiskel.rest;

import com.widiskel.rest.resolver.UserArgsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration  implements WebMvcConfigurer {


    @Autowired
    private UserArgsResolver userArgsResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(userArgsResolver);

    }
}
