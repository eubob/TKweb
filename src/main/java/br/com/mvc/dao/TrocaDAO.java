package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Troca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrocaDAO {
    public boolean criar(Troca troca) {
        String sql = "INSERT INTO trocas (solicitante_id, receptor_id, habilidade_solicitada_id, habilidade_oferecida_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, troca.getSolicitanteId());
            stmt.setLong(2, troca.getReceptorId());
            stmt.setLong(3, troca.getHabilidadeSolicitadaId());
            stmt.setLong(4, troca.getHabilidadeOferecidaId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Troca> listarPorUsuario(Long usuarioId) {
        List<Troca> result = new ArrayList<>();
        String sql = """
                SELECT t.id, t.solicitante_id, t.receptor_id,
                       us.nome solicitante_nome, ur.nome receptor_nome,
                       t.habilidade_solicitada_id, hs.titulo habilidade_solicitada_titulo,
                       t.habilidade_oferecida_id, ho.titulo habilidade_oferecida_titulo,
                       t.status, t.criada_em, t.atualizada_em,
                       EXISTS (SELECT 1 FROM avaliacoes a WHERE a.troca_id = t.id AND a.avaliador_id = ?) avaliacao_enviada
                FROM trocas t
                JOIN usuarios us ON us.id = t.solicitante_id
                JOIN usuarios ur ON ur.id = t.receptor_id
                JOIN habilidades hs ON hs.id = t.habilidade_solicitada_id
                JOIN habilidades ho ON ho.id = t.habilidade_oferecida_id
                WHERE t.solicitante_id = ? OR t.receptor_id = ?
                ORDER BY t.atualizada_em DESC, t.id DESC
                """;
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setLong(2, usuarioId);
            stmt.setLong(3, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapear(rs));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    public Troca buscar(Long trocaId) {
        String sql = "SELECT t.id, t.solicitante_id, t.receptor_id, us.nome solicitante_nome, ur.nome receptor_nome, t.habilidade_solicitada_id, hs.titulo habilidade_solicitada_titulo, t.habilidade_oferecida_id, ho.titulo habilidade_oferecida_titulo, t.status, t.criada_em, t.atualizada_em FROM trocas t JOIN usuarios us ON us.id=t.solicitante_id JOIN usuarios ur ON ur.id=t.receptor_id JOIN habilidades hs ON hs.id=t.habilidade_solicitada_id JOIN habilidades ho ON ho.id=t.habilidade_oferecida_id WHERE t.id=?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, trocaId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean usuarioParticipa(Long trocaId, Long usuarioId) {
        String sql = "SELECT id FROM trocas WHERE id = ? AND (solicitante_id = ? OR receptor_id = ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, trocaId);
            stmt.setLong(2, usuarioId);
            stmt.setLong(3, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean alterarStatus(Long trocaId, Long usuarioId, String status) {
        String sql;
        if ("ACEITA".equals(status) || "RECUSADA".equals(status)) {
            sql = "UPDATE trocas SET status=? WHERE id=? AND receptor_id=? AND status='PENDENTE'";
        } else if ("FINALIZADA".equals(status)) {
            sql = "UPDATE trocas SET status=? WHERE id=? AND (solicitante_id=? OR receptor_id=?) AND status='ACEITA'";
        } else {
            return false;
        }
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, trocaId);
            stmt.setLong(3, usuarioId);
            if ("FINALIZADA".equals(status)) stmt.setLong(4, usuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Troca mapear(ResultSet rs) throws SQLException {
        Troca t = new Troca();
        t.setId(rs.getLong("id"));
        t.setSolicitanteId(rs.getLong("solicitante_id"));
        t.setReceptorId(rs.getLong("receptor_id"));
        t.setSolicitanteNome(rs.getString("solicitante_nome"));
        t.setReceptorNome(rs.getString("receptor_nome"));
        t.setHabilidadeSolicitadaId(rs.getLong("habilidade_solicitada_id"));
        t.setHabilidadeSolicitadaTitulo(rs.getString("habilidade_solicitada_titulo"));
        t.setHabilidadeOferecidaId(rs.getLong("habilidade_oferecida_id"));
        t.setHabilidadeOferecidaTitulo(rs.getString("habilidade_oferecida_titulo"));
        t.setStatus(rs.getString("status"));
        t.setCriadaEm(rs.getTimestamp("criada_em"));
        t.setAtualizadaEm(rs.getTimestamp("atualizada_em"));
        try { t.setAvaliacaoEnviada(rs.getBoolean("avaliacao_enviada")); } catch (SQLException ignored) { }
        return t;
    }
}
