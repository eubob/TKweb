package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.NotificacaoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/notificacoes")
public class NotificacaoServlet extends HttpServlet {
    private final NotificacaoService service = new NotificacaoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        request.setAttribute("notificacoes", service.listar(usuario.getId()));
        service.marcarTodasLidas(usuario.getId());
        request.getRequestDispatcher("/notificacoes.jsp").forward(request, response);
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
