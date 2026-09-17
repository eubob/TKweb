package br.com.mvc.service;

import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean cadastrarUsuario(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            return false;
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            return false;
        }

        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            return false;
        }

        usuario.setNome(usuario.getNome().trim());
        usuario.setEmail(usuario.getEmail().trim());

        return usuarioDAO.cadastrar(usuario);
    }


    public Usuario autenticar(String nome, String senha) {

        if (nome == null || nome.trim().isEmpty()) {
            return null;
        }

        if (senha == null || senha.trim().isEmpty()) {
            return null;
        }

        nome = nome.trim();

        return usuarioDAO.autenticar(nome, senha);
    }
}