package com.its.memberboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String connectPath = "/memberUpload/**";
    private String resourcePath = "file:////Users/taeyeonlee/developer/spring_boot/member_img/";

    private String connectPath1 = "/boardUpload/**";
    private String resourcePath1 = "file:////Users/taeyeonlee/developer/spring_boot/board_img/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);
        registry.addResourceHandler(connectPath1)
                .addResourceLocations(resourcePath1);
    }
}
