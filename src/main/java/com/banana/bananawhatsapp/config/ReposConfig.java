package com.banana.bananawhatsapp.config;

import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import com.banana.bananawhatsapp.persistencia.MensajeRepository;
import com.banana.bananawhatsapp.persistencia.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReposConfig {

    @Value("${db_url}")
    String dbUrl;
    @Bean
    //@Profile("default")
    public IUsuarioRepository getUsuarioRepository() throws Exception {
        System.out.println("Repos usuario config " + dbUrl);
        UsuarioRepository repo = new UsuarioRepository();
        repo.setDb_url(dbUrl);
        return repo;
    }
    @Bean
    //@Profile("default")
    public IMensajeRepository getMensajeRepository() throws Exception {
        System.out.println("Repos mensaje config " + dbUrl);
        MensajeRepository repo = new MensajeRepository();
        repo.setDb_url(dbUrl);
        return repo;
    }
}
