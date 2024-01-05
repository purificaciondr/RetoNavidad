package com.banana.bananawhatsapp.servicios;

import com.banana.bananawhatsapp.exceptions.MensajeException;
import com.banana.bananawhatsapp.exceptions.UsuarioException;
import com.banana.bananawhatsapp.modelos.Mensaje;
import com.banana.bananawhatsapp.modelos.Usuario;
import com.banana.bananawhatsapp.persistencia.IMensajeRepository;
import com.banana.bananawhatsapp.persistencia.IUsuarioRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Setter
public class ServicioMensajeria implements IServicioMensajeria{
    IMensajeRepository mensajeRepo;

    @Autowired
    IServicioUsuarios servicioUsuarios;
    @Override
    public Mensaje enviarMensaje(Usuario remitente, Usuario destinatario, String texto) throws UsuarioException, MensajeException {
        Set<Usuario> listaDesti = servicioUsuarios.obtenerPosiblesDesinatarios(remitente, 50);
        boolean destEncontrado = false;
        for (Usuario us: listaDesti) {
            if (us.getId() == destinatario.getId()) { destEncontrado = true; }
        }
        if (!destEncontrado) {
            throw new UsuarioException("Usuario destino no activo");
        }
        Mensaje msg = new Mensaje(1, remitente, destinatario, texto, LocalDate.now());
        try {
            mensajeRepo.crear(msg);
        } catch (Exception e) {
            throw new MensajeException("Mensaje no creado");
        }

        return msg;
    }

    @Override
    public List<Mensaje> mostrarChatConUsuario(Usuario remitente, Usuario destinatario) throws UsuarioException, MensajeException {
        servicioUsuarios.obtenerPosiblesDesinatarios(destinatario, 50);  // para validar destinatario

        List<Mensaje> listChatDesti = new ArrayList<>();
        try {
            List<Mensaje> listaMensajes = mensajeRepo.obtener(remitente);
            for (Mensaje msg: listaMensajes) {
                if (msg.getDestinatario().getId().equals(destinatario.getId())
                || msg.getRemitente().getId().equals(destinatario.getId())) {
                    listChatDesti.add(msg);
                }
            }
        } catch (SQLException e) {
            throw new MensajeException(e.getMessage());
        }
        if (listChatDesti.isEmpty()) {
            throw new MensajeException("Remitente y destinatario sin chat comun");
        }
        return listChatDesti;
    }

    @Override
    public boolean borrarChatConUsuario(Usuario remitente, Usuario destinatario) throws UsuarioException, MensajeException {
        try {
            mostrarChatConUsuario(remitente,destinatario);
            mensajeRepo.borrarTodos(remitente,destinatario);
        } catch (SQLException e) {
            throw new MensajeException(e.getMessage());
        }
        return true;
    }
}
