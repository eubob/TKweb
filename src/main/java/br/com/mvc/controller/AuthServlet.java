package br.com.mvc.controller;

import br.com.mvc.dao.UsuarioDAO;
import br.com.mvc.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        if ("cadastrar".equals(action)) {
            handleCadastro(request, response);
        } else if ("login".equals(action)) {
            handleLogin(request, response);
        }
    }

    private void handleCadastro(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = new Usuario(nome, email, senha);
        if (usuarioDAO.cadastrar(usuario)) {
            request.setAttribute("msgSucesso", "Cadastro realizado com sucesso! Faça login.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("msgErro", "Erro ao cadastrar usuário. Tente novamente.");
            request.getRequestDispatcher("cadastro.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        String nome = request.getParameter("nome");
        String senha = request.getParameter("senha");

        Usuario usuario = usuarioDAO.autenticar(nome, senha);
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);
            response.sendRedirect("home.jsp");
        } else {
            request.setAttribute("msgErro", "Nome ou senha inválidos.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}