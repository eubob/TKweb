package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Mensagem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MensagemDAO {
    public boolean enviar(Mensagem mensagem) {
        String sql = "INSERT INTO mensagens (troca_id, remetente_id, mensagem) VALUES (?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, mensagem.getTrocaId());
            stmt.setLong(2, mensagem.getRemetenteId());
            stmt.setString(3, mensagem.getMensagem());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Mensagem> listarPorTroca(Long trocaId) {
        List<Mensagem> result = new ArrayList<>();
        String sql = "SELECT m.id, m.troca_id, m.remetente_id, u.nome remetente_nome, m.mensagem, m.enviada_em FROM mensagens m JOIN usuarios u ON u.id=m.remetente_id WHERE m.troca_id=? ORDER BY m.enviada_em ASC, m.id ASC";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, trocaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mensagem m = new Mensagem();
                    m.setId(rs.getLong("id"));
                    m.setTrocaId(rs.getLong("troca_id"));
                    m.setRemetenteId(rs.getLong("remetente_id"));
                    m.setRemetenteNome(rs.getString("remetente_nome"));
                    m.setMensagem(rs.getString("mensagem"));
                    m.setEnviadaEm(rs.getTimestamp("enviada_em"));
                    result.add(m);
                }
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }
}
