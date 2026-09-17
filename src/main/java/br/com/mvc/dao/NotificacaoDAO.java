package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Notificacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoDAO {
    public boolean criar(Long usuarioId, String tipo, String mensagem, String url) {
        String sql = "INSERT INTO notificacoes (usuario_id, tipo, mensagem, url) VALUES (?, ?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setString(2, tipo);
            stmt.setString(3, mensagem);
            stmt.setString(4, url);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Notificacao> listar(Long usuarioId) {
        List<Notificacao> result = new ArrayList<>();
        String sql = "SELECT id, tipo, mensagem, url, lida, criada_em FROM notificacoes WHERE usuario_id=? ORDER BY criada_em DESC, id DESC LIMIT 50";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notificacao n = new Notificacao();
                    n.setId(rs.getLong("id"));
                    n.setTipo(rs.getString("tipo"));
                    n.setMensagem(rs.getString("mensagem"));
                    n.setUrl(rs.getString("url"));
                    n.setLida(rs.getBoolean("lida"));
                    n.setCriadaEm(rs.getTimestamp("criada_em"));
                    result.add(n);
                }
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    public int contarNaoLidas(Long usuarioId) {
        String sql = "SELECT COUNT(*) FROM notificacoes WHERE usuario_id=? AND lida=0";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        } catch (SQLException e) {
            return 0;
        }
    }

    public boolean marcarTodasLidas(Long usuarioId) {
        String sql = "UPDATE notificacoes SET lida=1 WHERE usuario_id=? AND lida=0";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
