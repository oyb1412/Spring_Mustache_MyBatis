package kr.co.myproject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/files/**") 
            .addResourceLocations("file:///C:/JavaProject/files/");

        registry
            .addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/");


        registry
            .addResourceHandler("/images/**")
            .addResourceLocations("classpath:/static/images/");
    }
}