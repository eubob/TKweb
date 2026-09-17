package br.com.mvc.service;

import br.com.mvc.dao.MensagemDAO;
import br.com.mvc.model.Mensagem;

import java.util.List;

public class ChatService {
    private final MensagemDAO dao = new MensagemDAO();
    private final TrocaService trocaService = new TrocaService();

    public List<Mensagem> listar(Long trocaId, Long usuarioId) {
        if (trocaId == null || usuarioId == null || !trocaService.participa(trocaId, usuarioId)) return List.of();
        return dao.listarPorTroca(trocaId);
    }

    public boolean enviar(Long trocaId, Long usuarioId, String mensagem) {
        if (trocaId == null || usuarioId == null || mensagem == null || mensagem.trim().isEmpty()) return false;
        if (!trocaService.participa(trocaId, usuarioId)) return false;
        TrocaServiceStatus status = status(trocaId);
        if (!status.permitido) return false;
        mensagem = mensagem.trim();
        if (mensagem.length() > 1000) mensagem = mensagem.substring(0, 1000);
        Mensagem m = new Mensagem();
        m.setTrocaId(trocaId);
        m.setRemetenteId(usuarioId);
        m.setMensagem(mensagem);
        return dao.enviar(m);
    }

    private TrocaServiceStatus status(Long trocaId) {
        var troca = trocaService.buscar(trocaId);
        return new TrocaServiceStatus(troca != null && ("ACEITA".equals(troca.getStatus()) || "FINALIZADA".equals(troca.getStatus())));
    }

    private record TrocaServiceStatus(boolean permitido) { }
}
