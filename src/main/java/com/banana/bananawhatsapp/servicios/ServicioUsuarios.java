package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ServicioUsuarios implements IServicioUsuarios{
    @Autowired
    IUsuarioRepository usuarioRepo;
    @Override
    public Usuario crearUsuario(Usuario usuario) throws UsuarioException {
        if (!usuario.valido()) {
            throw new UsuarioException();
        } else {
            try {
                usuarioRepo.crear(usuario);
            } catch (SQLException e) {
                throw new UsuarioException(e.getMessage());
            }

        }

        return usuario;
    }

    @Override
    public boolean borrarUsuario(Usuario usuario) throws UsuarioException {
        return false;
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) throws UsuarioException {
        return null;
    }

    @Override
    public Set<Usuario> obtenerPosiblesDesinatarios(Usuario usuario, int max) throws UsuarioException {
        Set<Usuario> listaDestinatario = new HashSet<>();
        try {
            listaDestinatario = usuarioRepo.obtenerPosiblesDestinatarios(usuario.getId(),max);
            if (listaDestinatario.isEmpty()) {
                throw new UsuarioException("Usuario sin destinatarios activos");
            }
        } catch (SQLException e) {
            throw new UsuarioException(e.getMessage());
        }
        return listaDestinatario;
    }

}
