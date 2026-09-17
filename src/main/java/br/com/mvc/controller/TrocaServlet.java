package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.TrocaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/trocas")
public class TrocaServlet extends HttpServlet {
    private final TrocaService service = new TrocaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        request.setAttribute("trocas", service.listar(usuario.getId()));
        request.getRequestDispatcher("/trocas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        try {
            Long id = Long.parseLong(request.getParameter("trocaId"));
            String action = request.getParameter("action");
            if ("aceitar".equals(action)) service.aceita(id, usuario.getId());
            else if ("recusar".equals(action)) service.recusa(id, usuario.getId());
            else if ("finalizar".equals(action)) service.finalizar(id, usuario.getId());
        } catch (NumberFormatException ignored) { }
        response.sendRedirect(request.getContextPath() + "/trocas");
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
