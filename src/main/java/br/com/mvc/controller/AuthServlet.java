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

    private static final long serialVersionUID = 1L;

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            request.setAttribute("msgErro", "Ação não informada.");
            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
            return;
        }

        switch (action) {

            case "cadastrar":
                handleCadastro(request, response);
                break;

            case "login":
                handleLogin(request, response);
                break;

            default:
                request.setAttribute("msgErro", "Ação inválida.");
                request.getRequestDispatcher("/login.jsp")
                        .forward(request, response);
                break;
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("logout".equals(action)) {

            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        response.sendRedirect(
                request.getContextPath() + "/login.jsp"
        );
    }


    private void handleCadastro(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()) {

            request.setAttribute(
                    "msgErro",
                    "Preencha todos os campos."
            );

            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);

            return;
        }

        Usuario usuario = new Usuario(
                nome.trim(),
                email.trim(),
                senha
        );

        boolean cadastrado =
                usuarioService.cadastrarUsuario(usuario);

        if (cadastrado) {

            request.setAttribute(
                    "msgSucesso",
                    "Cadastro realizado com sucesso! Faça login."
            );

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);

        } else {

            request.setAttribute(
                    "msgErro",
                    "Não foi possível realizar o cadastro. "
                    + "Verifique os dados informados."
            );

            request.getRequestDispatcher("/cadastro.jsp")
                    .forward(request, response);
        }
    }

    private void handleLogin(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String senha = request.getParameter("senha");

        if (nome == null || nome.trim().isEmpty()
                || senha == null || senha.trim().isEmpty()) {

            request.setAttribute(
                    "msgErro",
                    "Informe o nome e a senha."
            );

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);

            return;
        }

        Usuario usuario =
                usuarioService.autenticar(
                        nome.trim(),
                        senha
                );

        if (usuario != null) {

            HttpSession session = request.getSession();

            session.setAttribute(
                    "usuarioLogado",
                    usuario
            );

            response.sendRedirect(
                    request.getContextPath() + "/home.jsp"
            );

        } else {

            request.setAttribute(
                    "msgErro",
                    "Nome ou senha inválidos."
            );

            request.getRequestDispatcher("/login.jsp")
                    .forward(request, response);
        }
    }
}