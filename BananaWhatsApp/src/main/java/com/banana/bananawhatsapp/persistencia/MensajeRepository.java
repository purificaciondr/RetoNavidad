package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;

import java.sql.SQLException;
import java.util.List;

public class MensajeRepository implements IMensajeRepository{
    @Override
    public Mensaje crear(Mensaje mensaje) throws SQLException {
        return null;
    }

    @Override
    public List<Mensaje> obtener(Usuario usuario) throws SQLException {
        return null;
    }

    @Override
    public boolean borrarTodos(Usuario usuario) throws SQLException {
        return false;
    }
}
