package com.banana.bananawhatsapp.persistencia;
import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.modelos.Mensaje;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("default")
class MensajeRepositoryTest {
    @Autowired
    IMensajeRepository repo;
    @Autowired
    IUsuarioRepository repoU;

    @Test
    void dadoUnMensajeValido_cuandoCrear_entoncesMensajeValido() throws SQLException {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(2);
        Mensaje msj1 = new Mensaje(1,us1,us2, "Hola 2, esto es una prueba de envio de mensajes", LocalDate.now());
        repo.crear(msj1);
        System.out.println(msj1);
        assertThat(msj1.getId(), greaterThan(0));
    }

    @Test
    void dadoUnMensajeNOValido_cuandoCrear_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(2);
        Mensaje msj1 = new Mensaje(1,us1,us2, "msg corto", LocalDate.now());
        assertThrows(Exception.class, () -> {
            repo.crear(msj1);
        });
        Mensaje msj2 = new Mensaje(1,null,us2, "msg correcto", LocalDate.now());
        assertThrows(Exception.class, () -> {
            repo.crear(msj2);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoObtener_entoncesListaMensajes() {
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtener_entoncesExcepcion() {
    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrarTodos_entoncesOK() {
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrarTodos_entoncesExcepcion() {
    }

}