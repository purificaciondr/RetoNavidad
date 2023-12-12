package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.properties.PropertyValues;

import java.io.IOException;
import java.sql.*;
import java.util.Set;

public class UsuarioRepository implements IUsuarioRepository{
    private String db_url = null;
    public UsuarioRepository() throws IOException {
        PropertyValues props = new PropertyValues();
        db_url = props.getPropValues().getProperty("db_url");
    }
    @Override
    public Usuario crear(Usuario usuario) throws SQLException {

        Connection conn = null;
        try {
            usuario.valido();
            conn = DriverManager.getConnection(db_url);
            //conn.setAutoCommit(false);
            // insertamos usuario
            String sql = "INSERT INTO usuario VALUES(NULL,?,?,?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, 1);   // activo
            pstm.setString(2, usuario.getAlta().toString());
            pstm.setString(3, usuario.getEmail());
            pstm.setString(4, usuario.getNombre());

            int rows = pstm.executeUpdate();

            ResultSet generatedKeys = pstm.getGeneratedKeys();
            if (generatedKeys.next()) {
                usuario.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating usuario failed, no ID obtained.");
            }
            pstm.close();

        } catch (Exception e) {
            System.out.println("Transaccion rollback!!");
            conn.rollback();
            e.printStackTrace();
            throw e;

        } finally {
            if (conn != null) conn.close();
        }


        return usuario;
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        return null;
    }

    @Override
    public boolean borrar(Usuario usuario) throws SQLException {
        return false;
    }

    @Override
    public Set<Usuario> obtenerPosiblesDestinatarios(Integer id, Integer max) throws SQLException {
        return null;
    }
}
