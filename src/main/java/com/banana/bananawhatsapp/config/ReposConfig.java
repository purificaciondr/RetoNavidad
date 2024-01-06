package com.banana.bananawhatsapp.config;

import com.banana.bananawhatsapp.persistencia.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ReposConfig {

    @Value("${db_url}")
    String dbUrl;
    @Bean
    @Profile("default")
    public IUsuarioRepository getUsuarioRepository() throws Exception {
        UsuarioRepository repo = new UsuarioRepository();
        repo.setDb_url(dbUrl);
        return repo;
    }
    @Bean
    @Profile("dev")
    public IUsuarioRepository getUsuarioInMemoryRepository() throws Exception {
        UsuarioInMemoryRepository repo = new UsuarioInMemoryRepository();
        return repo;
    }
    @Bean
    public IMensajeRepository getMensajeRepository() throws Exception {
        System.out.println("Repos mensaje config " + dbUrl);
        MensajeRepository repo = new MensajeRepository();
        repo.setDb_url(dbUrl);
        return repo;
    }
}
