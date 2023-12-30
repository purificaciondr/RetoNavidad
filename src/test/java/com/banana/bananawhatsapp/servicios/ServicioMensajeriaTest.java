package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("default")

class ServicioMensajeriaTest {
    @Autowired
    IServicioMensajeria servicio;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Test
    void dadoRemitenteYDestinatarioYTextoValido_cuandoEnviarMensaje_entoncesMensajeValido() throws SQLException {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Set<Usuario> posDest = usuarioRepo.obtenerPosiblesDestinatarios(1,3);
        Usuario us2 = posDest.iterator().next();
        System.out.println(us2);
        Mensaje msg = servicio.enviarMensaje(us1, us2, "Texto de mas de 10 palabras en el cuerpo del msg");
        assertThat(msg.getId(), greaterThan(0));
    }


    @Test
    void dadoRemitenteYDestinatarioYTextoNOValido_cuandoEnviarMensaje_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(22);
        assertThrows(Exception.class, () -> {
            servicio.enviarMensaje(us1, us2, "Msm corto");
        });
    }

    @Test
    void dadoRemitenteYDestinatarioValido_cuandoMostrarChatConUsuario_entoncesListaMensajes() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(2);
        List<Mensaje> listaMensajes = servicio.mostrarChatConUsuario(us1, us2);
        assertThat(listaMensajes.size(), greaterThan(0));
    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoMostrarChatConUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(3);
        assertThrows(Exception.class, () -> {
            servicio.mostrarChatConUsuario(us1, us2);
        });
    }

    @Test
    void dadoRemitenteYDestinatarioValido_cuandoBorrarChatConUsuario_entoncesOK() {

    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoBorrarChatConUsuario_entoncesExcepcion() {

    }
}