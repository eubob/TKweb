package br.com.mvc.service;

import br.com.mvc.dao.HabilidadeDAO;
import br.com.mvc.model.Habilidade;

import java.util.List;

public class HabilidadeService {

    private final HabilidadeDAO habilidadeDAO;

    public HabilidadeService() {
        this.habilidadeDAO = new HabilidadeDAO();
    }

    public boolean cadastrar(Habilidade habilidade) {

        if (habilidade == null) {
            return false;
        }

        if (habilidade.getUsuarioId() == null) {
            return false;
        }

        if (habilidade.getTitulo() == null ||
                habilidade.getTitulo().trim().isEmpty()) {
            return false;
        }

        if (habilidade.getTipo() == null ||
                habilidade.getTipo().trim().isEmpty()) {
            return false;
        }

        String tipo = habilidade.getTipo()
                .trim()
                .toUpperCase();

        if (!tipo.equals("OFERECE") &&
                !tipo.equals("DESEJA")) {
            return false;
        }

        habilidade.setTitulo(
                habilidade.getTitulo().trim()
        );

        habilidade.setTipo(tipo);

        if (habilidade.getDescricao() != null) {
            habilidade.setDescricao(
                    habilidade.getDescricao().trim()
            );
        }

        return habilidadeDAO.cadastrar(habilidade);
    }

    public List<Habilidade> listarPorUsuario(Long usuarioId) {

        if (usuarioId == null) {
            return List.of();
        }

        return habilidadeDAO.listarPorUsuario(usuarioId);
    }

    public Habilidade buscarPorId(
            Long id,
            Long usuarioId
    ) {

        if (id == null || usuarioId == null) {
            return null;
        }

        return habilidadeDAO.buscarPorId(
                id,
                usuarioId
        );
    }

    public boolean atualizar(Habilidade habilidade) {

        if (habilidade == null) {
            return false;
        }

        if (habilidade.getId() == null ||
                habilidade.getUsuarioId() == null) {
            return false;
        }

        if (habilidade.getTitulo() == null ||
                habilidade.getTitulo().trim().isEmpty()) {
            return false;
        }

        if (habilidade.getTipo() == null ||
                habilidade.getTipo().trim().isEmpty()) {
            return false;
        }

        String tipo = habilidade.getTipo()
                .trim()
                .toUpperCase();

        if (!tipo.equals("OFERECE") &&
                !tipo.equals("DESEJA")) {
            return false;
        }

        habilidade.setTitulo(
                habilidade.getTitulo().trim()
        );

        habilidade.setTipo(tipo);

        if (habilidade.getDescricao() != null) {
            habilidade.setDescricao(
                    habilidade.getDescricao().trim()
            );
        }

        return habilidadeDAO.atualizar(habilidade);
    }

    public boolean excluir(
            Long id,
            Long usuarioId
    ) {

        if (id == null || usuarioId == null) {
            return false;
        }

        return habilidadeDAO.excluir(
                id,
                usuarioId
        );
    }
}