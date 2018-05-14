package server.mvc;


import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Mapuje widoki
     * @param registry
     */
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/panel").setViewName("admin_panel");
        registry.addViewController("/info").setViewName("info");
        registry.addViewController("/aktualny").setViewName("data");
        registry.addViewController("/reset").setViewName("reset");
        registry.addViewController("/register").setViewName("register");

    }


}