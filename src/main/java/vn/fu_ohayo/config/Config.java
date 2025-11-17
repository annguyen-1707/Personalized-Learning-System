package vn.fu_ohayo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class Config implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("file:src/main/webapp/resources/images/");
        registry.addResourceHandler("/audio/**").addResourceLocations("file:src/main/webapp/resources/audio/");
        registry.addResourceHandler("/url/**").addResourceLocations("/resources/url/");
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
        registry.addResourceHandler("/video/**").addResourceLocations("file:src/main/webapp/resources/video/");
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}