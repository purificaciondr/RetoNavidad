package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("dev")
class ControladorUsuariosTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Autowired
    ControladorUsuarios controladorUsuarios;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    void dadoUsuarioValido_cuandoAlta_entoncesUsuarioValido() {
        Usuario us1 = new Usuario(3,"prueba", "p@p.com", LocalDate.now(), true);
        controladorUsuarios.alta(us1);
        assertThat(outContent.toString(), containsString("Usuario creado"));
    }

    @Test
    void dadoUsuarioNOValido_cuandoAlta_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba", "pp.com", LocalDate.now(), true);
        assertThrows(Exception.class, () -> {
            controladorUsuarios.alta(us1);
        });
    }

    @Test
    void dadoUsuarioValido_cuandoActualizar_entoncesUsuarioValido() {
        Usuario us1 = new Usuario(3,"prueba udpcont", "u@control.com", LocalDate.now(), true);
        us1.setId(7);
        controladorUsuarios.actualizar(us1);
        assertThat(outContent.toString(), containsString("Usuario actualizado"));
    }

    @Test
    void dadoUsuarioNOValido_cuandoActualizar_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba udpcont", "ucontrol.com", LocalDate.now(), true);
        us1.setId(7);
        assertThrows(Exception.class, () -> {
            controladorUsuarios.actualizar(us1);
        });
    }

    @Test
    void dadoUsuarioValido_cuandoBaja_entoncesUsuarioValido() throws SQLException {
        // montamos chat que queremos borrar
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr rmt", "r@r.com", LocalDate.now(), true));
        Usuario us2 = usuarioRepo.crear(new Usuario(1, "prueba borr dst", "d@d.com", LocalDate.now(), true));
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            mensajeRepo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            mensajeRepo.crear(msg2);
        }

        controladorUsuarios.baja(us1);
        assertThat(outContent.toString(), containsString("Usuario borrado"));
    }

    @Test
    void dadoUsuarioNOValido_cuandoBaja_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(3);
        assertThrows(Exception.class, () -> {
            controladorUsuarios.baja(us1);
        });

    }
}