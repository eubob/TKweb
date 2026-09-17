package br.com.mvc.service;

import br.com.mvc.dao.NotificacaoDAO;
import br.com.mvc.model.Notificacao;

import java.util.List;

public class NotificacaoService {
    private final NotificacaoDAO dao = new NotificacaoDAO();
    public List<Notificacao> listar(Long usuarioId) { return dao.listar(usuarioId); }
    public int contarNaoLidas(Long usuarioId) { return dao.contarNaoLidas(usuarioId); }
    public boolean marcarTodasLidas(Long usuarioId) { return dao.marcarTodasLidas(usuarioId); }
}
