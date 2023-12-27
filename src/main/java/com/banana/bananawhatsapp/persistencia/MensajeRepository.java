package com.banana.bananawhatsapp.persistencia;

import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import lombok.Setter;

import java.sql.*;
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
            conn.setAutoCommit(false);
            // obtenemos remitente
            String sql = "SELECT * FROM usuario WHERE id=?";
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setInt(1, mensaje.getRemitente().getId());
            ResultSet rs = pstm.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Usuario remitente no encontrado");
            }
            pstm.close();
            // obtenemos remitente
            sql = "SELECT * FROM usuario WHERE id=?";
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, mensaje.getDestinatario().getId());
            rs = pstm.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Usuario destinatario no encontrado");
            }
            pstm.close();

            // insertamos mensaje
            sql = "INSERT INTO mensaje VALUES(NULL,?,?,?,?)";
            pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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

        return mensaje;
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
