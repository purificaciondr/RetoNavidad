package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Setter
public class MensajeRepository implements IMensajeRepository{
    private String db_url;
    @Override
    public Mensaje crear(Mensaje mensaje) throws SQLException {
        System.out.println("dentro de repositorio mensaje " + db_url);
        Connection conn = null;
        try {
            mensaje.valido();
            conn = DriverManager.getConnection(db_url);
            //conn.setAutoCommit(false);

            // insertamos mensaje
            String sql = "INSERT INTO mensaje VALUES(NULL,?,?,?,?)";
            PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, mensaje.getCuerpo());
            pstm.setString(2, mensaje.getFecha().toString());
            pstm.setInt(3, mensaje.getRemitente().getId());
            pstm.setInt(4, mensaje.getDestinatario().getId());
            int rows = pstm.executeUpdate();

            ResultSet generatedKeys = pstm.getGeneratedKeys();

            if (generatedKeys.next()) {
                mensaje.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating usuario failed, no ID obtained.");
            }
            pstm.close();
            //conn.commit();

        } catch (Exception e) {
            System.out.println("Transaccion rollback!!");
            conn.rollback();
            e.printStackTrace();
            System.out.println("mensaje " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) conn.close();
        }

        return mensaje;
    }

    @Override
    public List<Mensaje> obtener(Usuario usuario) throws SQLException {
        List<Mensaje> listaMensajes = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(db_url);
            String sql = "SELECT * FROM usuario WHERE id=? AND activo=?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, usuario.getId());
            pstm.setInt(2, 1);
            ResultSet rs = pstm.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Usuario no activo");
            }
            pstm.close();

            //conn.setAutoCommit(false);
            sql = "SELECT * FROM mensaje WHERE from_user=? OR to_user=? ORDER BY id";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, usuario.getId());
            pstm.setInt(2, usuario.getId());
            rs = pstm.executeQuery();
            while (rs.next()) {
                Usuario us1 = new Usuario();
                us1.setId(rs.getInt("from_user"));
                Usuario us2 = new Usuario();
                us2.setId(rs.getInt("to_user"));
                listaMensajes.add(new Mensaje(
                        rs.getInt("id"),
                        us1,
                        us2,
                        rs.getString("cuerpo"),
                        rs.getDate("fecha").toLocalDate()
                    ));
            }
            pstm.close();

        } catch (Exception e) {
            System.out.println("Transaccion rollback!!");
            conn.rollback();
            e.printStackTrace();
            System.out.println("mensaje " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
        return listaMensajes;
    }

    @Override
    public boolean borrarTodos(Usuario usuario) throws SQLException {
        return false;
    }
}
