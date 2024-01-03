package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.config.SpringConfig;
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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@ActiveProfiles("default")
class UsuarioRepositoryTest {
    @Autowired
    IUsuarioRepository repo;

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
    void dadoUnUsuarioValido_cuandoBorrar_entoncesOK() {
    }

    @Test
    void dadoUnUsuarioNOValido_cuandoBorrar_entoncesExcepcion() {
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