package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("default")
class ServicioUsuariosTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Autowired
    IServicioUsuarios servicio;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;

    @Test
    void dadoUnUsuarioValido_cuandoCrearUsuario_entoncesUsuarioValido() {
        Usuario us1 = new Usuario(3,"prueba", "p@p.com", LocalDate.now(), true);
        servicio.crearUsuario(us1);
        assertThat(us1.getId(), greaterThan(0));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoCrearUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba", "pp.com", LocalDate.now(), true);
        assertThrows(UsuarioException.class, () -> {
            servicio.crearUsuario(us1);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrarUsuario_entoncesUsuarioValido() {
        Usuario us1 = new Usuario(7,"prueba upt", "u@u.com", LocalDate.now(), true);
        us1.setId(7);
        Usuario udtUsu = servicio.actualizarUsuario(us1);
        assertEquals(us1.getEmail(),udtUsu.getEmail());
        assertEquals(us1.getNombre(),udtUsu.getNombre());
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrarUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario(7,"prueba upt", "uu.com", LocalDate.now(), true);
        us1.setId(7);
        assertThrows(UsuarioException.class, () -> {
            servicio.actualizarUsuario(us1);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoActualizarUsuario_entoncesUsuarioValido() throws SQLException {
        // montamos chat que queremos borrar
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr rmt", "r@r.com", LocalDate.now(), true));
        Usuario us2 = new Usuario();
        us2.setId(1);
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            mensajeRepo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            mensajeRepo.crear(msg2);
        }

        boolean ok = servicio.borrarUsuario(us1);
        assertThat(ok,is(true));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoActualizarUsuario_entoncesExcepcion() throws SQLException {
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr usu", "r@r.com", LocalDate.now(), true));
        us1.setId(3);  // usuario inexistente

        assertThrows(UsuarioException.class, () -> {
            servicio.borrarUsuario(us1);
        } );
    }

    @Test
    void dadoUnUsuarioValido_cuandoObtenerPosiblesDesinatarios_entoncesUsuariosValidos() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Set<Usuario> listaDesti =  servicio.obtenerPosiblesDesinatarios(us1, 3);
        assertThat(us1.getId(), greaterThan(0));
        assertThat(listaDesti.size(),lessThan(4));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtenerPosiblesDesinatarios_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(3);
        assertThrows(UsuarioException.class, () -> {
            servicio.crearUsuario(us1);
        });
    }
}