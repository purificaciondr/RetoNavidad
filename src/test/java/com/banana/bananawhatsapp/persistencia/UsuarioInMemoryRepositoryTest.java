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
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("dev")
class UsuarioInMemoryRepositoryTest {
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Autowired
    IMensajeRepository mensajeRepo;

    @Test
    void dadoUnUsuarioValido_cuandoCrear_entoncesUsuarioValido() throws SQLException {
        Usuario us1 = new Usuario(3,"prueba", "p@p.com", LocalDate.now(), true);
        Usuario usRespue = usuarioRepo.crear(us1);
        System.out.println(us1);
        assertThat(usRespue.getId(), greaterThan(0));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoCrear_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba", "pp.com", LocalDate.now(), true);
        assertThrows(Exception.class, () -> {
            usuarioRepo.crear(us1);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoActualizar_entoncesUsuarioValido() throws SQLException {
        Usuario us1 = usuarioRepo.crear(new Usuario(7,"prueba upt", "u@u.com", LocalDate.now(), true));

        Usuario udtUsu = usuarioRepo.actualizar(us1);
        assertEquals(us1.getEmail(),udtUsu.getEmail());
        assertEquals(us1.getNombre(),udtUsu.getNombre());

    }

    @Test
    void dadoUnUsuarioNOValido_cuandoActualizar_entoncesExcepcion() {
        Usuario us1 = new Usuario(7,"prueba upt", "uu.com", LocalDate.now(), true);
        us1.setId(7);
        assertThrows(SQLException.class, () -> {
            usuarioRepo.crear(us1);
        });

    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrar_entoncesOK() throws SQLException {
        // creamos usuario y chat que queremos borrar
        Usuario us1 = usuarioRepo.crear(new Usuario(1, "prueba borr usu", "r@r.com", LocalDate.now(), true));
        Usuario us2 = usuarioRepo.crear(new Usuario(2,"prueba borr ", "u@u.com", LocalDate.now(), true));
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            mensajeRepo.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            mensajeRepo.crear(msg2);
        }

        boolean ok = usuarioRepo.borrar(us1);
        assertThat(ok,is(true));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrar_entoncesExcepcion() {
        Usuario us1 = new Usuario();
        us1.setId(3);  // usuario inexistente

        assertThrows(Exception.class, () -> {
            usuarioRepo.borrar(us1);
        } );
    }

    @Test
    void dadoUnUsuarioValido_cuandoObtenerPosiblesDestinatarios_entoncesLista() throws SQLException {
        Usuario us1 = usuarioRepo.crear(new Usuario(1,"prueba obt", "u@u.com", LocalDate.now(), true));
        Usuario us2 = usuarioRepo.crear(new Usuario(2,"prueba obt", "u@u.com", LocalDate.now(), true));
        Set<Usuario> listaDesti = usuarioRepo.obtenerPosiblesDestinatarios(us1.getId(), 3);
        assertThat(listaDesti.size(), greaterThan(0));
        assertThat(listaDesti.size(),lessThan(4));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtenerPosiblesDestinatarios_entoncesExcepcion() {

         assertThrows(Exception.class, () -> {
            usuarioRepo.obtenerPosiblesDestinatarios(3, 3);  // usuario 3 inexistente
        });
    }

}