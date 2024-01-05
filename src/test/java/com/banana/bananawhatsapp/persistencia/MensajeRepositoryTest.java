package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
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
        Mensaje msj1Respue = repo.crear(msj1);
        System.out.println(msj1Respue);
        assertThat(msj1Respue.getId(), greaterThan(0));
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
    void dadoUnUsuarioValido_cuandoObtener_entoncesListaMensajes() throws SQLException {
        Usuario us1 = new Usuario();
        us1.setId(1);
        List<Mensaje> listaMensa =  repo.obtener(us1);
        assertThat(listaMensa.size(), greaterThan(0));
        System.out.println(listaMensa);
        for (Mensaje msg: listaMensa) {
            System.out.println(msg.getCuerpo());
        }
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtener_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(3);
        assertThrows(Exception.class, () -> {
            repo.obtener(us1);
        });

    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrarTodos_entoncesOK() throws SQLException {
        // montamos chat que queremos borrar
        Usuario us1 = repoU.crear(new Usuario(1, "prueba borr rmt", "r@r.com", LocalDate.now(), true));
        Usuario us2 = repoU.crear(new Usuario(1, "prueba borr dst", "d@d.com", LocalDate.now(), true));
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            repo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            repo.crear(msg2);
        }


        //repo.borrarTodos(us1);
        boolean ok = repo.borrarTodos(us1, us2);
        assertThat(ok,is(true));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrarTodos_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(1);
        Usuario us2 = new Usuario();
        us2.setId(3);  // no existe
        assertThrows(Exception.class, () -> {
            repo.borrarTodos(us1, us2);
        });
    }

}