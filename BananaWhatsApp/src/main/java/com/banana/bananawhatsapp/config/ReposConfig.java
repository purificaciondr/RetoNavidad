package com.banana.bananawhatsapp.config;

import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
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
        System.out.println("bean de bd");
        UsuarioRepository repo = new UsuarioRepository();
        //repo.setDb_url(connUrl);
        return repo;
    }
}
