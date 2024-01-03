package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.config.SpringConfig;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("dev")
class ControladorMensajesTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    @Autowired
    ControladorMensajes controladorMensajes;
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;

    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    @Test
    void dadoRemitenteYDestinatarioYTextoValidos_cuandoEnviarMensaje_entoncesOK() {
        boolean enviado;
        enviado = controladorMensajes.enviarMensaje(1,2,"Texto de mas de 10 palabras en el cuerpo del msg" );
        assertTrue(enviado);
        //assertThat(outContent.toString(), containsString("Mensaje enviado"));
    }

    @Test
    void dadoRemitenteYDestinatarioYTextoNOValidos_cuandoEnviarMensaje_entoncesExcepcion() {
        assertThrows(Exception.class, () -> {
            controladorMensajes.enviarMensaje(1,3,"Msg corto" );
        });


    }

    @Test
    void dadoRemitenteYDestinatarioValidos_cuandoMostrarChat_entoncesOK() {
        boolean enviado;
        enviado = controladorMensajes.mostrarChat(1,2);
        assertTrue(enviado);

    }

    @Test
    void dadoRemitenteYDestinatarioNOValidos_cuandoMostrarChat_entoncesExcepcion() {
         assertThrows(Exception.class, () -> {
            controladorMensajes.mostrarChat(1,3);
        });

    }

    @Test
    void dadoRemitenteYDestinatarioValidos_cuandoEliminarChatConUsuario_entoncesOK() throws SQLException {
        boolean borrado;

        // montamos chat que queremos borrar
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr rmt", "r@r.com", LocalDate.now(), true));
        Usuario us2 = usuarioRepo.crear(new Usuario(1, "prueba borr dst", "d@d.com", LocalDate.now(), true));
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            mensajeRepo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            mensajeRepo.crear(msg2);
        }

        borrado = controladorMensajes.eliminarChatConUsuario(us1.getId(), us2.getId());
        assertTrue(borrado);

    }

    @Test
    void dadoRemitenteYDestinatarioNOValidos_cuandoEliminarChatConUsuario_entoncesExcepcion() {
         assertThrows(Exception.class, () -> {
            controladorMensajes.eliminarChatConUsuario(1,3);
        });
    }
}