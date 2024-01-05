package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.modelos.Usuario;
import lombok.Setter;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
@Setter
public class UsuarioRepository implements IUsuarioRepository{
    private String db_url;

    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        System.out.println("dentro de repositorio " + db_url);
        Connection conn = null;
        try {
            usuario.valido();
            conn = DriverManager.getConnection(db_url);
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
        Connection conn = null;
        try {
            usuario.valido();
            conn = DriverManager.getConnection(db_url);
            // insertamos usuario
            String sql = "UPDATE usuario SET email=?,nombre=?,activo=? WHERE id=?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, usuario.getEmail());
            pstm.setString(2, usuario.getNombre());
            pstm.setInt(3, usuario.isActivo() ? 1 : 0);
            pstm.setInt(4,usuario.getId());

            int rows = pstm.executeUpdate();
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
    public boolean borrar(Usuario usuario) throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db_url);
            conn.setAutoCommit(false);
            // delete de los mensajes del usuario  para poder eliminar la relacion
            String sql = "DELETE FROM mensaje WHERE from_user=? OR to_user=?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, usuario.getId());
            pstm.setInt(2, usuario.getId());
            int rows = pstm.executeUpdate();
            pstm.close();

            // borramos usuario
            sql = "DELETE FROM usuario WHERE id=?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, usuario.getId());
            rows = pstm.executeUpdate();
            if (rows <=0) {
                throw new SQLException("Filas no borradas.");
            }
            pstm.close();
            conn.commit();

        } catch (Exception e) {
            System.out.println("Transaccion rollback!!");
            conn.rollback();
            e.printStackTrace();
            System.out.println("mensaje " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) conn.close();
        }

        return true;
    }

    @Override
    public Set<Usuario> obtenerPosiblesDestinatarios(Integer id, Integer max) throws SQLException {
        Set<Usuario> listaDesti = new HashSet<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db_url);
            // Consultamos usuario
            String sql = "SELECT * FROM usuario WHERE id = ? AND activo = ?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            pstm.setInt(2, 1); // activo

            ResultSet rs = pstm.executeQuery();
            if (!rs.next())  {
                throw new SQLException("Usuario " + id + " no activo");
            }

            // insertamos usuario
            sql = "SELECT * FROM usuario WHERE id <>? AND activo = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, id);
            pstm.setInt(2, 1); // activo

            rs = pstm.executeQuery();
            while (rs.next() && listaDesti.size()<max) {
                listaDesti.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getDate("alta").toLocalDate(),
                        rs.getBoolean("activo")
                ));
            }
            pstm.close();

        } catch (Exception e) {
            System.out.println("Transaccion rollback!!");
            e.printStackTrace();
            throw e;

        } finally {
            if (conn != null) conn.close();
        }

        return listaDesti;
    }
}
