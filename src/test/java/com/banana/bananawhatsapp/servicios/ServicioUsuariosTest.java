package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("dev")
class ServicioUsuariosTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Autowired
    IServicioUsuarios servicio;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    @Test
    void dadoUnUsuarioValido_cuandoCrearUsuario_entoncesUsuarioValido() {
        Usuario us1 = new Usuario(3,"prueba", "p@p.com", LocalDate.now(), true);
        servicio.crearUsuario(us1);
        assertThat(us1.getId(), greaterThan(0));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoCrearUsuario_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba", "pp.com", LocalDate.now(), true);
        assertThrows(Exception.class, () -> {
            servicio.crearUsuario(us1);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrarUsuario_entoncesUsuarioValido() {
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrarUsuario_entoncesExcepcion() {
    }

    @Test
    void dadoUnUsuarioValido_cuandoActualizarUsuario_entoncesUsuarioValido() {

    }

    @Test
    void dadoUnUsuarioNOValido_cuandoActualizarUsuario_entoncesExcepcion() {
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
        assertThrows(Exception.class, () -> {
            servicio.crearUsuario(us1);
        });
    }
}