package br.com.mvc.service;

import br.com.mvc.dao.AvaliacaoDAO;
import br.com.mvc.dao.NotificacaoDAO;
import br.com.mvc.model.Avaliacao;
import br.com.mvc.model.Troca;

public class AvaliacaoService {
    private final AvaliacaoDAO dao = new AvaliacaoDAO();
    private final TrocaService trocaService = new TrocaService();
    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    public boolean salvar(Long trocaId, Long avaliadorId, int nota, String comentario) {
        Troca troca = trocaService.buscar(trocaId);
        if (troca == null || !"FINALIZADA".equals(troca.getStatus())) return false;
        if (!troca.getSolicitanteId().equals(avaliadorId) && !troca.getReceptorId().equals(avaliadorId)) return false;
        if (dao.jaAvaliou(trocaId, avaliadorId) || nota < 1 || nota > 5) return false;
        Long avaliadoId = troca.getSolicitanteId().equals(avaliadorId) ? troca.getReceptorId() : troca.getSolicitanteId();
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setTrocaId(trocaId);
        avaliacao.setAvaliadorId(avaliadorId);
        avaliacao.setAvaliadoId(avaliadoId);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario == null ? "" : comentario.trim());
        boolean ok = dao.salvar(avaliacao);
        if (ok) notificacaoDAO.criar(avaliadoId, "AVALIACAO", "Você recebeu uma nova avaliação.", "perfil?id=" + avaliadoId);
        return ok;
    }
}
