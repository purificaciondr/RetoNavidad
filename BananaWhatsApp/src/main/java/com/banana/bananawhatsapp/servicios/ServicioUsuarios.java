package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

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
    public Usuario obtenerPosiblesDesinatarios(Usuario usuario, int max) throws UsuarioException {
        return null;
    }
}
