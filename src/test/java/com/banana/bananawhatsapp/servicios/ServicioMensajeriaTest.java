package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("default")

class ServicioMensajeriaTest {
    @Autowired
    IServicioMensajeria servicio;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;
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
        assertThrows(MensajeException.class, () -> {
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
        System.out.println(listaMensajes);
        assertThat(listaMensajes.size(), greaterThan(0));

    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoMostrarChatConUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(3);  // no existe
        Usuario us3 = new Usuario();
        us3.setId(7);  // sin chat comun con 1

        assertThrows(UsuarioException.class, () -> {
            servicio.mostrarChatConUsuario(us1, us2);
        });
        assertThrows(MensajeException.class, () -> {
            servicio.mostrarChatConUsuario(us1, us3);
        });
    }

    @Test
    void dadoRemitenteYDestinatarioValido_cuandoBorrarChatConUsuario_entoncesOK() throws SQLException {
        // montamos chat que queremos borrar
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr rmt", "r@r.com", LocalDate.now(), true));
        Usuario us2 = usuarioRepo.crear(new Usuario(1, "prueba borr dst", "d@d.com", LocalDate.now(), true));
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            mensajeRepo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            mensajeRepo.crear(msg2);
        }

        boolean ok = servicio.borrarChatConUsuario(us1, us2);
        assertThat(ok,is(true));
    }

    @Test
    void dadoRemitenteYDestinatarioNOValido_cuandoBorrarChatConUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(3);
        assertThrows(UsuarioException.class, () -> {
            servicio.borrarChatConUsuario(us1, us2);
        });
        Usuario us3 = new Usuario();
        us3.setId(7);
        assertThrows(MensajeException.class, () -> {
            servicio.borrarChatConUsuario(us1, us3);
        });
    }
}