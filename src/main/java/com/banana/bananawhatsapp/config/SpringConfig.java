package com.banana.bananawhatsapp.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertiesConfig.class, ReposConfig.class, ServiceConfig.class})
@ComponentScan(basePackages = {"com.banana.persistence", "com.banana.services", "com.banana.bananawhatsapp.controladores"})
public class SpringConfig {
}
