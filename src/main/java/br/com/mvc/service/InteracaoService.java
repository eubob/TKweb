package br.com.mvc.service;

import br.com.mvc.dao.InteracaoDAO;
import br.com.mvc.dao.NotificacaoDAO;
import br.com.mvc.model.Comentario;

import java.util.List;

public class InteracaoService {
    private final InteracaoDAO dao = new InteracaoDAO();
    private final HabilidadeService habilidadeService = new HabilidadeService();
    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    public boolean alternarCurtida(Long habilidadeId, Long usuarioId) {
        if (habilidadeId == null || usuarioId == null) return false;
        var habilidade = habilidadeService.buscarPublica(habilidadeId);
        if (habilidade == null) return false;
        boolean curtida = dao.alternarCurtida(habilidadeId, usuarioId);
        if (curtida && !usuarioId.equals(habilidade.getUsuarioId())) notificacaoDAO.criar(habilidade.getUsuarioId(), "CURTIDA", "Sua publicação recebeu uma curtida.", "feed?categoria=" + java.net.URLEncoder.encode(habilidade.getCategoria(), java.nio.charset.StandardCharsets.UTF_8));
        return curtida;
    }

    public boolean comentar(Long habilidadeId, Long usuarioId, String texto) {
        if (habilidadeId == null || usuarioId == null || texto == null || texto.trim().isEmpty()) return false;
        if (habilidadeService.buscarPublica(habilidadeId) == null) return false;
        texto = texto.trim();
        if (texto.length() > 500) texto = texto.substring(0, 500);
        boolean ok = dao.adicionarComentario(habilidadeId, usuarioId, texto);
        if (ok && !usuarioId.equals(habilidadeService.buscarPublica(habilidadeId).getUsuarioId())) notificacaoDAO.criar(habilidadeService.buscarPublica(habilidadeId).getUsuarioId(), "COMENTARIO", "Sua publicação recebeu um novo comentário.", "feed?categoria=" + java.net.URLEncoder.encode(habilidadeService.buscarPublica(habilidadeId).getCategoria(), java.nio.charset.StandardCharsets.UTF_8));
        return ok;
    }

    public List<Comentario> comentarios(Long habilidadeId) {
        return dao.listarComentarios(habilidadeId);
    }
}
