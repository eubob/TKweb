package br.com.mvc.dao;

import br.com.mvc.config.MysqlSingleton;
import br.com.mvc.model.Habilidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HabilidadeDAO {
    public boolean cadastrar(Habilidade habilidade) {
        String sql = "INSERT INTO habilidades (titulo, descricao, tipo, categoria, usuario_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, habilidade.getTitulo());
            stmt.setString(2, habilidade.getDescricao());
            stmt.setString(3, habilidade.getTipo());
            stmt.setString(4, habilidade.getCategoria());
            stmt.setLong(5, habilidade.getUsuarioId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Habilidade> listarPorUsuario(Long usuarioId) {
        String sql = "SELECT id, titulo, descricao, tipo, categoria, usuario_id, criado_em FROM habilidades WHERE usuario_id = ? ORDER BY criado_em DESC, id DESC";
        return listar(sql, usuarioId);
    }

    public Habilidade buscarPorId(Long id, Long usuarioId) {
        String sql = "SELECT id, titulo, descricao, tipo, categoria, usuario_id, criado_em FROM habilidades WHERE id = ? AND usuario_id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public Habilidade buscarPublica(Long id) {
        String sql = "SELECT h.id, h.titulo, h.descricao, h.tipo, h.categoria, h.usuario_id, h.criado_em, u.nome usuario_nome FROM habilidades h JOIN usuarios u ON u.id = h.usuario_id WHERE h.id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean atualizar(Habilidade habilidade) {
        String sql = "UPDATE habilidades SET titulo = ?, descricao = ?, tipo = ?, categoria = ? WHERE id = ? AND usuario_id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, habilidade.getTitulo());
            stmt.setString(2, habilidade.getDescricao());
            stmt.setString(3, habilidade.getTipo());
            stmt.setString(4, habilidade.getCategoria());
            stmt.setLong(5, habilidade.getId());
            stmt.setLong(6, habilidade.getUsuarioId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean excluir(Long id, Long usuarioId) {
        String sql = "DELETE FROM habilidades WHERE id = ? AND usuario_id = ?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.setLong(2, usuarioId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Habilidade> listarFeed(String categoria, String tipo, Long usuarioId) {
        StringBuilder sql = new StringBuilder("""
                SELECT h.id, h.titulo, h.descricao, h.tipo, h.categoria, h.usuario_id, h.criado_em,
                       u.nome usuario_nome,
                       COUNT(DISTINCT c.id) comentarios,
                       COUNT(DISTINCT l.id) curtidas,
                       MAX(CASE WHEN l.usuario_id = ? THEN 1 ELSE 0 END) curtida_usuario
                FROM habilidades h
                JOIN usuarios u ON u.id = h.usuario_id
                LEFT JOIN comentarios c ON c.habilidade_id = h.id
                LEFT JOIN curtidas l ON l.habilidade_id = h.id
                WHERE 1 = 1
                """);
        List<Object> params = new ArrayList<>();
        params.add(usuarioId);
        if (categoria != null && !categoria.isBlank()) {
            sql.append(" AND h.categoria = ?");
            params.add(categoria);
        }
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND h.tipo = ?");
            params.add(tipo);
        }
        sql.append(" GROUP BY h.id, h.titulo, h.descricao, h.tipo, h.categoria, h.usuario_id, h.criado_em, u.nome ORDER BY h.criado_em DESC, h.id DESC");
        return listarComParametros(sql.toString(), params);
    }

    public List<Habilidade> listarSugestoes(Long usuarioId, int limite) {
        String sql = """
                SELECT DISTINCT h.id, h.titulo, h.descricao, h.tipo, h.categoria, h.usuario_id, h.criado_em, u.nome usuario_nome
                FROM habilidades h
                JOIN usuarios u ON u.id = h.usuario_id
                WHERE h.usuario_id <> ?
                  AND h.tipo = 'OFERECE'
                  AND h.categoria IN (
                      SELECT categoria FROM habilidades WHERE usuario_id = ? AND tipo = 'DESEJA'
                  )
                ORDER BY h.criado_em DESC, h.id DESC
                LIMIT ?
                """;
        List<Habilidade> result = new ArrayList<>();
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            stmt.setLong(2, usuarioId);
            stmt.setInt(3, limite);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapear(rs));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }


    public List<Long> listarUsuariosInteressados(String categoria, Long excluirUsuarioId) {
        List<Long> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT usuario_id FROM habilidades WHERE tipo='DESEJA' AND categoria=? AND usuario_id<>?";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            stmt.setLong(2, excluirUsuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ids.add(rs.getLong(1));
            }
        } catch (SQLException e) {
            return ids;
        }
        return ids;
    }

    public List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM habilidades ORDER BY categoria";
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) categorias.add(rs.getString(1));
        } catch (SQLException e) {
            return categorias;
        }
        return categorias;
    }

    private List<Habilidade> listar(String sql, Long usuarioId) {
        List<Habilidade> result = new ArrayList<>();
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapear(rs));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    private List<Habilidade> listarComParametros(String sql, List<Object> params) {
        List<Habilidade> result = new ArrayList<>();
        try (Connection conn = MysqlSingleton.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof Long value) stmt.setLong(i + 1, value);
                else stmt.setString(i + 1, String.valueOf(param));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) result.add(mapear(rs));
            }
        } catch (SQLException e) {
            return result;
        }
        return result;
    }

    private Habilidade mapear(ResultSet rs) throws SQLException {
        Habilidade h = new Habilidade();
        h.setId(rs.getLong("id"));
        h.setTitulo(rs.getString("titulo"));
        h.setDescricao(rs.getString("descricao"));
        h.setTipo(rs.getString("tipo"));
        h.setCategoria(rs.getString("categoria"));
        h.setUsuarioId(rs.getLong("usuario_id"));
        h.setCriadoEm(rs.getTimestamp("criado_em"));
        try { h.setUsuarioNome(rs.getString("usuario_nome")); } catch (SQLException ignored) { }
        try { h.setCurtidas(rs.getInt("curtidas")); } catch (SQLException ignored) { }
        try { h.setComentarios(rs.getInt("comentarios")); } catch (SQLException ignored) { }
        try { h.setCurtidaPeloUsuario(rs.getInt("curtida_usuario") > 0); } catch (SQLException ignored) { }
        return h;
    }
}
