package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InteracaoDAO {
    public boolean alternarCurtida(Long habilidadeId, Long usuarioId) {
        String select = "SELECT id FROM curtidas WHERE habilidade_id = ? AND usuario_id = ?";
        String insert = "INSERT INTO curtidas (habilidade_id, usuario_id) VALUES (?, ?)";
        String delete = "DELETE FROM curtidas WHERE habilidade_id = ? AND usuario_id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement find = conn.prepareStatement(select)) {
            find.setLong(1, habilidadeId);
            find.setLong(2, usuarioId);
            try (ResultSet rs = find.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement stmt = conn.prepareStatement(delete)) {
                        stmt.setLong(1, habilidadeId);
                        stmt.setLong(2, usuarioId);
                        stmt.executeUpdate();
                    }
                    return false;
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setLong(1, habilidadeId);
                stmt.setLong(2, usuarioId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean adicionarComentario(Long habilidadeId, Long usuarioId, String texto) {
        String sql = "INSERT INTO comentarios (habilidade_id, usuario_id, texto) VALUES (?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, habilidadeId);
            stmt.setLong(2, usuarioId);
            stmt.setString(3, texto);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Comentario> listarComentarios(Long habilidadeId) {
        List<Comentario> result = new ArrayList<>();
        String sql = "SELECT c.id, c.habilidade_id, c.usuario_id, u.nome usuario_nome, c.texto, c.criado_em FROM comentarios c JOIN usuarios u ON u.id = c.usuario_id WHERE c.habilidade_id = ? ORDER BY c.criado_em DESC";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, habilidadeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comentario c = new Comentario();
                    c.setId(rs.getLong("id"));
                    c.setHabilidadeId(rs.getLong("habilidade_id"));
                    c.setUsuarioId(rs.getLong("usuario_id"));
                    c.setUsuarioNome(rs.getString("usuario_nome"));
                    c.setTexto(rs.getString("texto"));
                    c.setCriadoEm(rs.getTimestamp("criado_em"));
                    result.add(c);
                }
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }
}
