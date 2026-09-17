package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    public boolean cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Usuario autenticar(String identificador, String senhaHash, String senhaOriginal) {
        String sql = "SELECT id, nome, email, senha FROM usuarios WHERE email = ? OR nome = ? LIMIT 1";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, identificador);
            stmt.setString(2, identificador);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String senhaBanco = rs.getString("senha");
                    boolean valida = senhaHash.equals(senhaBanco) || senhaOriginal.equals(senhaBanco);
                    if (!valida) return null;
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    if (senhaOriginal.equals(senhaBanco)) atualizarSenha(rs.getLong("id"), senhaHash);
                    return usuario;
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    private void atualizarSenha(Long usuarioId, String senhaHash) {
        String sql = "UPDATE usuarios SET senha=? WHERE id=?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, senhaHash);
            stmt.setLong(2, usuarioId);
            stmt.executeUpdate();
        } catch (SQLException ignored) { }
    }

    public Usuario buscarPorId(Long id) {
        String sql = "SELECT id, nome, email FROM usuarios WHERE id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setEmail(rs.getString("email"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT id FROM usuarios WHERE email = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public void carregarReputacao(Usuario usuario) {
        String sql = "SELECT COALESCE(AVG(nota),0), COUNT(*) FROM avaliacoes WHERE avaliado_id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuario.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario.setMediaAvaliacao(rs.getDouble(1));
                    usuario.setTotalAvaliacoes(rs.getInt(2));
                }
            }
        } catch (SQLException e) {
            usuario.setMediaAvaliacao(0);
            usuario.setTotalAvaliacoes(0);
        }
    }

    public int calcularPontos(Long usuarioId) {
        String sql = """
                SELECT
                    (SELECT COUNT(*) * 10 FROM habilidades WHERE usuario_id = ? AND tipo = 'OFERECE') +
                    (SELECT COUNT(*) * 5 FROM habilidades WHERE usuario_id = ? AND tipo = 'DESEJA') +
                    (SELECT COUNT(*) * 30 FROM trocas WHERE status = 'FINALIZADA' AND (solicitante_id = ? OR receptor_id = ?)) +
                    (SELECT COUNT(*) FROM comentarios WHERE usuario_id = ?) +
                    (SELECT COUNT(*) FROM curtidas WHERE usuario_id = ?) AS pontos
                """;
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) stmt.setLong(i, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("pontos");
            }
        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }
}
