package br.com.mvc.service;

import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Usuario;
import br.com.mvc.util.PasswordUtil;

public class UsuarioService {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean cadastrarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getNome() == null || usuario.getEmail() == null || usuario.getSenha() == null) return false;
        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim().toLowerCase());
        if (usuario.getNome().isBlank() || usuario.getEmail().isBlank() || usuario.getSenha().isBlank()) return false;
        if (usuarioDAO.emailExiste(usuario.getEmail())) return false;
        usuario.setSenha(PasswordUtil.hash(usuario.getSenha()));
        return usuarioDAO.cadastrar(usuario);
    }

    public Usuario autenticar(String email, String senha) {
        if (email == null || senha == null || email.isBlank() || senha.isBlank()) return null;
        Usuario usuario = usuarioDAO.autenticar(email.trim(), PasswordUtil.hash(senha), senha);
        if (usuario != null) {
            usuarioDAO.carregarReputacao(usuario);
            usuario.setPontos(usuarioDAO.calcularPontos(usuario.getId()));
        }
        return usuario;
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) return null;
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            usuarioDAO.carregarReputacao(usuario);
            usuario.setPontos(usuarioDAO.calcularPontos(id));
        }
        return usuario;
    }
}
