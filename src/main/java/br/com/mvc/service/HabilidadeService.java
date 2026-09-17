package br.com.mvc.service;

import br.com.mvc.dao.HabilidadeDAO;
import br.com.mvc.dao.NotificacaoDAO;
import br.com.mvc.model.Habilidade;

import java.util.List;

public class HabilidadeService {
    public static final String[] CATEGORIAS = {"Idiomas", "Tecnologia", "Arte", "Música", "Culinária", "Negócios", "Acadêmico", "Esportes", "Artesanato", "Outros"};
    private final HabilidadeDAO dao = new HabilidadeDAO();
    private final NotificacaoDAO notificacaoDAO = new NotificacaoDAO();

    public boolean cadastrar(Habilidade habilidade) {
        if (!validar(habilidade, false)) return false;
        boolean ok = dao.cadastrar(habilidade);
        if (ok) {
            for (Long usuarioId : dao.listarUsuariosInteressados(habilidade.getCategoria(), habilidade.getUsuarioId())) {
                notificacaoDAO.criar(usuarioId, "RECOMENDACAO", "Nova oferta de " + habilidade.getCategoria() + ": " + habilidade.getTitulo(), "feed?categoria=" + java.net.URLEncoder.encode(habilidade.getCategoria(), java.nio.charset.StandardCharsets.UTF_8));
            }
        }
        return ok;
    }

    public boolean atualizar(Habilidade habilidade) {
        if (!validar(habilidade, true)) return false;
        return dao.atualizar(habilidade);
    }

    public boolean excluir(Long id, Long usuarioId) {
        return id != null && usuarioId != null && dao.excluir(id, usuarioId);
    }

    public Habilidade buscarPorId(Long id, Long usuarioId) {
        if (id == null || usuarioId == null) return null;
        return dao.buscarPorId(id, usuarioId);
    }

    public Habilidade buscarPublica(Long id) {
        return id == null ? null : dao.buscarPublica(id);
    }

    public List<Habilidade> listarPorUsuario(Long usuarioId) {
        return usuarioId == null ? List.of() : dao.listarPorUsuario(usuarioId);
    }

    public List<Habilidade> listarFeed(String categoria, String tipo, Long usuarioId) {
        return dao.listarFeed(categoria, tipo, usuarioId);
    }

    public List<Habilidade> listarSugestoes(Long usuarioId, int limite) {
        return usuarioId == null ? List.of() : dao.listarSugestoes(usuarioId, limite);
    }

    public List<String> listarCategorias() {
        return dao.listarCategorias();
    }

    public java.util.Map<String, Integer> listarCategoriasMaisPopulares() {
        return dao.listarCategoriasMaisPopulares();
    }

    private boolean validar(Habilidade habilidade, boolean atualizar) {
        if (habilidade == null || habilidade.getUsuarioId() == null) return false;
        if (atualizar && habilidade.getId() == null) return false;
        if (habilidade.getTitulo() == null || habilidade.getTitulo().trim().isEmpty()) return false;
        if (habilidade.getDescricao() == null) habilidade.setDescricao("");
        if (habilidade.getTipo() == null) return false;
        String tipo = habilidade.getTipo().trim().toUpperCase();
        if (!tipo.equals("OFERECE") && !tipo.equals("DESEJA")) return false;
        if (habilidade.getCategoria() == null || habilidade.getCategoria().trim().isEmpty()) return false;
        habilidade.setTitulo(habilidade.getTitulo().trim());
        habilidade.setDescricao(habilidade.getDescricao().trim());
        habilidade.setTipo(tipo);
        habilidade.setCategoria(habilidade.getCategoria().trim());
        return true;
    }
}
