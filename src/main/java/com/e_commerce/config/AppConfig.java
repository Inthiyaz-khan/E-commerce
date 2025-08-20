package com.e_commerce.config;

import com.e_commerce.dto.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ProductResponse productResponse() {
        return new ProductResponse();
    }
}
