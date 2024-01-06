package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UsuarioInMemoryRepository implements IUsuarioRepository{
    List<Usuario> usuarios = new ArrayList<>();
    private static Integer num = 0;


    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        try {
            usuario.valido();
        } catch (UsuarioException e) {
            throw new SQLException(e.getMessage());
        }
        num++;
        usuario.setId(num);
        usuarios.add(usuario);

        return usuario;
    }


    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        boolean encontrado = false;
        try {
            usuario.valido();
            for (Usuario unUsuario : usuarios) {
                if (usuario.getId() == unUsuario.getId()) {
                    encontrado = true;
                    usuarios.remove(usuario);
                    usuarios.add(unUsuario);
                    return unUsuario;
                }
            }
            if (!encontrado) {
                throw new SQLException("Usuario no existente");
            }
        } catch (UsuarioException e) {
            throw new SQLException(e.getMessage());
        }

        return usuario;
    }

    @Override
    public boolean borrar(Usuario usuario) throws SQLException {
        boolean encontrado = false;
        try {
            usuario.valido();
            for (Usuario unUsuario : usuarios) {
                if (usuario.getId() == unUsuario.getId()) {
                    encontrado = true;
                    usuarios.remove(usuario);
                }
            }
        } catch (UsuarioException e) {
            throw new SQLException(e.getMessage());
        }
        return true;
    }

    @Override
    public Set<Usuario> obtenerPosiblesDestinatarios(Integer id, Integer max) throws SQLException {
        Set<Usuario> listaRes = new HashSet<>();
        boolean encontrado = false;
        for (Usuario unUsuario : usuarios) {
            if (id != unUsuario.getId() && unUsuario.isActivo()) {
                if (listaRes.size()<max) {
                    listaRes.add(unUsuario);
                }
            } else  {
                encontrado = true;
                if (!unUsuario.isActivo()) {
                    throw new SQLException("Usuario " + id + " no activo");
                }
            }
        }
        if (!encontrado) {
            throw new SQLException("Usuario " + id + " no activo");
        }
        return listaRes;
    }
}
