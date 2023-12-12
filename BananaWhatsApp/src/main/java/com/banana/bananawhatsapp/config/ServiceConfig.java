package com.banana.bananawhatsapp.config;

import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import com.banana.bananawhatsapp.servicios.IServicioUsuarios;
import com.banana.bananawhatsapp.servicios.ServicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Autowired
    IUsuarioRepository repo;
    @Bean
    public IServicioUsuarios getUsuarioService() {
        ServicioUsuarios usuSrv = new ServicioUsuarios();
        //usuSrv.setRepository(repo);
        return usuSrv;
    }
}
