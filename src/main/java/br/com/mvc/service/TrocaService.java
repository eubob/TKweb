package br.com.mvc.service;

import br.com.mvc.dao.HabilidadeDAO;
import br.com.mvc.dao.NotificacaoDAO;
import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.dao.TrocaDAO;
import br.com.mvc.model.Habilidade;
import br.com.mvc.model.Troca;

import java.util.List;

public class TrocaService {
    private final TrocaDAO trocaDAO = new TrocaDAO();
    private final HabilidadeDAO habilidadeDAO = new HabilidadeDAO();
    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean solicitar(Long solicitanteId, Long habilidadeSolicitadaId, Long habilidadeOferecidaId) {
        if (solicitanteId == null || habilidadeSolicitadaId == null || habilidadeOferecidaId == null) return false;
        Habilidade solicitada = habilidadeDAO.buscarPublica(habilidadeSolicitadaId);
        Habilidade oferecida = habilidadeDAO.buscarPorId(habilidadeOferecidaId, solicitanteId);
        if (solicitada == null || oferecida == null) return false;
        if (solicitada.getUsuarioId().equals(solicitanteId)) return false;
        if (!"OFERECE".equals(oferecida.getTipo())) return false;
        Troca troca = new Troca();
        troca.setSolicitanteId(solicitanteId);
        troca.setReceptorId(solicitada.getUsuarioId());
        troca.setHabilidadeSolicitadaId(solicitada.getId());
        troca.setHabilidadeOferecidaId(oferecida.getId());
        boolean ok = trocaDAO.criar(troca);
        if (ok) {
            String nome = usuarioDAO.buscarPorId(solicitanteId) != null ? usuarioDAO.buscarPorId(solicitanteId).getNome() : "um usuário";
            notificacaoDAO.criar(solicitada.getUsuarioId(), "TROCA", "Você recebeu uma nova solicitação de troca de " + nome + ".", "trocas");
        }
        return ok;
    }

    public List<Troca> listar(Long usuarioId) {
        return usuarioId == null ? List.of() : trocaDAO.listarPorUsuario(usuarioId);
    }

    public Troca buscar(Long trocaId) {
        return trocaId == null ? null : trocaDAO.buscar(trocaId);
    }

    public boolean aceita(Long trocaId, Long usuarioId) {
        boolean ok = trocaDAO.alterarStatus(trocaId, usuarioId, "ACEITA");
        notificarOutraParte(trocaId, usuarioId, ok, "A solicitação de troca foi aceita.");
        return ok;
    }

    public boolean recusa(Long trocaId, Long usuarioId) {
        boolean ok = trocaDAO.alterarStatus(trocaId, usuarioId, "RECUSADA");
        notificarOutraParte(trocaId, usuarioId, ok, "A solicitação de troca foi recusada.");
        return ok;
    }

    public boolean finalizar(Long trocaId, Long usuarioId) {
        boolean ok = trocaDAO.alterarStatus(trocaId, usuarioId, "FINALIZADA");
        notificarOutraParte(trocaId, usuarioId, ok, "A troca foi finalizada. Avalie a experiência.");
        return ok;
    }

    public boolean participa(Long trocaId, Long usuarioId) {
        return trocaDAO.usuarioParticipa(trocaId, usuarioId);
    }

    private void notificarOutraParte(Long trocaId, Long usuarioId, boolean ok, String mensagem) {
        if (!ok) return;
        Troca troca = trocaDAO.buscar(trocaId);
        if (troca == null) return;
        Long destino = troca.getSolicitanteId().equals(usuarioId) ? troca.getReceptorId() : troca.getSolicitanteId();
        notificacaoDAO.criar(destino, "TROCA", mensagem, "trocas");
    }
}
