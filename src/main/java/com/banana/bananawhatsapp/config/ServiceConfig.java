package com.banana.bananawhatsapp.config;

import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import com.banana.bananawhatsapp.servicios.IServicioMensajeria;
import com.banana.bananawhatsapp.servicios.IServicioUsuarios;
import com.banana.bananawhatsapp.servicios.ServicioMensajeria;
import com.banana.bananawhatsapp.servicios.ServicioUsuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;
    @Bean
    public IServicioUsuarios getUsuarioService() {
        ServicioUsuarios usuSrv = new ServicioUsuarios();
        usuSrv.setUsuarioRepo(usuarioRepo);
        return usuSrv;
    }
    @Bean
    public IServicioMensajeria getMensajeService() {
        ServicioMensajeria msgSrv = new ServicioMensajeria();
        msgSrv.setMensajeRepo(mensajeRepo);
        return msgSrv;
    }
}
