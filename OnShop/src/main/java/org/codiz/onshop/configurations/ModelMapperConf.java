package org.codiz.onshop.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class ModelMapperConf {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
