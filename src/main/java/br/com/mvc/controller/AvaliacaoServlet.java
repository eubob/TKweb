package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.AvaliacaoService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/avaliar")
public class AvaliacaoServlet extends HttpServlet {
    private final AvaliacaoService service = new AvaliacaoService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        try {
            Long trocaId = Long.parseLong(request.getParameter("trocaId"));
            int nota = Integer.parseInt(request.getParameter("nota"));
            service.salvar(trocaId, usuario.getId(), nota, request.getParameter("comentario"));
        } catch (NumberFormatException ignored) { }
        response.sendRedirect(request.getContextPath() + "/trocas");
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
