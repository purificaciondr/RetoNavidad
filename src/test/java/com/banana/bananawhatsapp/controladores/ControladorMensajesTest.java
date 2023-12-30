package com.banana.bananawhatsapp.controladores;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Usuario;
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
    void dadoRemitenteYDestinatarioValidos_cuandoEliminarChatConUsuario_entoncesOK() {
         assertThrows(Exception.class, () -> {
            controladorMensajes.enviarMensaje(1,2,"Msg corto" );
        });
    }

    @Test
    void dadoRemitenteYDestinatarioNOValidos_cuandoEliminarChatConUsuario_entoncesExcepcion() {
    }
}