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

    /**
     * CREATE
     */
    public boolean cadastrar(Habilidade habilidade) {

        String sql = """
                INSERT INTO habilidades
                (titulo, descricao, tipo, usuario_id)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection conn = MysqlSingleton.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, habilidade.getTitulo());
            stmt.setString(2, habilidade.getDescricao());
            stmt.setString(3, habilidade.getTipo());
            stmt.setLong(4, habilidade.getUsuarioId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * READ
     * Busca todas as habilidades de um usuário.
     */
    public List<Habilidade> listarPorUsuario(Long usuarioId) {

        List<Habilidade> habilidades = new ArrayList<>();

        String sql = """
                SELECT id, titulo, descricao, tipo, usuario_id
                FROM habilidades
                WHERE usuario_id = ?
                ORDER BY id DESC
                """;

        try (
                Connection conn = MysqlSingleton.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Habilidade habilidade = new Habilidade();

                    habilidade.setId(rs.getLong("id"));
                    habilidade.setTitulo(rs.getString("titulo"));
                    habilidade.setDescricao(rs.getString("descricao"));
                    habilidade.setTipo(rs.getString("tipo"));
                    habilidade.setUsuarioId(rs.getLong("usuario_id"));

                    habilidades.add(habilidade);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return habilidades;
    }

    /**
     * READ
     * Busca uma habilidade específica de um usuário.
     */
    public Habilidade buscarPorId(Long id, Long usuarioId) {

        String sql = """
                SELECT id, titulo, descricao, tipo, usuario_id
                FROM habilidades
                WHERE id = ?
                  AND usuario_id = ?
                """;

        try (
                Connection conn = MysqlSingleton.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);
            stmt.setLong(2, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Habilidade habilidade = new Habilidade();

                    habilidade.setId(rs.getLong("id"));
                    habilidade.setTitulo(rs.getString("titulo"));
                    habilidade.setDescricao(rs.getString("descricao"));
                    habilidade.setTipo(rs.getString("tipo"));
                    habilidade.setUsuarioId(rs.getLong("usuario_id"));

                    return habilidade;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    /**
     * UPDATE
     */
    public boolean atualizar(Habilidade habilidade) {

        String sql = """
                UPDATE habilidades
                SET titulo = ?,
                    descricao = ?,
                    tipo = ?
                WHERE id = ?
                  AND usuario_id = ?
                """;

        try (
                Connection conn = MysqlSingleton.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, habilidade.getTitulo());
            stmt.setString(2, habilidade.getDescricao());
            stmt.setString(3, habilidade.getTipo());
            stmt.setLong(4, habilidade.getId());
            stmt.setLong(5, habilidade.getUsuarioId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    /**
     * DELETE
     */
    public boolean excluir(Long id, Long usuarioId) {

        String sql = """
                DELETE FROM habilidades
                WHERE id = ?
                  AND usuario_id = ?
                """;

        try (
                Connection conn = MysqlSingleton.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);
            stmt.setLong(2, usuarioId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}