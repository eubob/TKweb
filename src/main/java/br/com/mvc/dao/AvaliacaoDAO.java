package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Avaliacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvaliacaoDAO {
    public boolean salvar(Avaliacao avaliacao) {
        String sql = "INSERT INTO avaliacoes (troca_id, avaliador_id, avaliado_id, nota, comentario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, avaliacao.getTrocaId());
            stmt.setLong(2, avaliacao.getAvaliadorId());
            stmt.setLong(3, avaliacao.getAvaliadoId());
            stmt.setInt(4, avaliacao.getNota());
            stmt.setString(5, avaliacao.getComentario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean jaAvaliou(Long trocaId, Long avaliadorId) {
        String sql = "SELECT id FROM avaliacoes WHERE troca_id=? AND avaliador_id=?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, trocaId);
            stmt.setLong(2, avaliadorId);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            return false;
        }
    }
}
