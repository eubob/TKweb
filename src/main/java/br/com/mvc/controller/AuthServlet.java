package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("logout".equals(request.getParameter("action"))) {
            HttpSession session = request.getSession(false);
            if (session != null) session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("cadastrar".equals(action)) cadastrar(request, response);
        else if ("login".equals(action)) login(request, response);
        else response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    private void cadastrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        if (nome == null || email == null || senha == null || nome.isBlank() || email.isBlank() || senha.length() < 6) {
            request.setAttribute("msgErro", "Informe nome, e-mail e uma senha com pelo menos 6 caracteres.");
            request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
            return;
        }
        Usuario usuario = new Usuario(nome, email, senha);
        if (usuarioService.cadastrarUsuario(usuario)) {
            request.setAttribute("msgSucesso", "Cadastro realizado com sucesso. Faça login para continuar.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            request.setAttribute("msgErro", "Não foi possível cadastrar. O e-mail pode já estar em uso.");
            request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        Usuario usuario = usuarioService.autenticar(email, senha);
        if (usuario != null) {
            request.getSession(true).setAttribute("usuarioLogado", usuario);
            response.sendRedirect(request.getContextPath() + "/feed");
        } else {
            request.setAttribute("msgErro", "E-mail ou senha inválidos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
