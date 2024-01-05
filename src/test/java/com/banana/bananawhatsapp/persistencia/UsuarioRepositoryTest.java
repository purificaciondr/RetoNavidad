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
@ActiveProfiles("default")
class UsuarioRepositoryTest {
    @Autowired
    IUsuarioRepository repo;
    @Autowired
    IMensajeRepository repoM;
    @Test
    void testBeans() {
        assertNotNull(repo);
        //System.out.println(repo.getDb_url());
    }
    @Test
    void dadoUnUsuarioValido_cuandoCrear_entoncesUsuarioValido() throws SQLException {
        Usuario us1 = new Usuario(3,"prueba", "p@p.com", LocalDate.now(), true);
        Usuario usRespue = repo.crear(us1);
        System.out.println(us1);
        assertThat(usRespue.getId(), greaterThan(0));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoCrear_entoncesExcepcion() {
        Usuario us1 = new Usuario(3,"prueba", "pp.com", LocalDate.now(), true);
        assertThrows(Exception.class, () -> {
            repo.crear(us1);
        });
    }

    @Test
    void dadoUnUsuarioValido_cuandoActualizar_entoncesUsuarioValido() throws SQLException {
        Usuario us1 = new Usuario(7,"prueba upt", "u@u.com", LocalDate.now(), true);
        us1.setId(7);
        Usuario udtUsu = repo.actualizar(us1);
        assertEquals(us1.getEmail(),udtUsu.getEmail());
        assertEquals(us1.getNombre(),udtUsu.getNombre());

    }

    @Test
    void dadoUnUsuarioNOValido_cuandoActualizar_entoncesExcepcion() {
        Usuario us1 = new Usuario(7,"prueba upt", "uu.com", LocalDate.now(), true);
        us1.setId(7);
        assertThrows(Exception.class, () -> {
            repo.crear(us1);
        });

    }

    @Test
    void dadoUnUsuarioValido_cuandoBorrar_entoncesOK() throws SQLException {
        // creamos usuario y chat que queremos borrar
        Usuario us1 = repo.crear(new Usuario(1, "prueba borr usu", "r@r.com", LocalDate.now(), true));
        Usuario us2 = new Usuario();
        us2.setId(1);
        for (int i = 0; i < 10; i++) {
            Mensaje msg = new Mensaje(1,us1, us2, "prueba test " + i, LocalDate.now());
            repoM.crear(msg);
            Mensaje msg2 = new Mensaje(1,us2, us1, "prueba test respuesta " + i, LocalDate.now());
            repoM.crear(msg2);
        }

        boolean ok = repo.borrar(us1);
        assertThat(ok,is(true));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrar_entoncesExcepcion() throws SQLException {
        Usuario us1 = repo.crear(new Usuario(1, "prueba borr usu", "r@r.com", LocalDate.now(), true));
        us1.setId(3);  // usuario inexistente

        assertThrows(Exception.class, () -> {
            repo.borrar(us1);
        } );
    }

    @Test
    void dadoUnUsuarioValido_cuandoObtenerPosiblesDestinatarios_entoncesLista() throws SQLException {
        Set<Usuario> listaDesti = repo.obtenerPosiblesDestinatarios(1, 3);
        System.out.println(listaDesti);
        assertThat(listaDesti.size(), greaterThan(0));
        assertThat(listaDesti.size(),lessThan(4));
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoObtenerPosiblesDestinatarios_entoncesExcepcion() {

         assertThrows(Exception.class, () -> {
            repo.obtenerPosiblesDestinatarios(3, 3);  // usuario 3 inexistente
        });
    }

}