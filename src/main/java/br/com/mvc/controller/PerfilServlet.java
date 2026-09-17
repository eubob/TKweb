package br.com.mvc.controller;

import br.com.mvc.model.Usuario;
import br.com.mvc.service.HabilidadeService;
import br.com.mvc.service.TrocaService;
import br.com.mvc.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    private final UsuarioService usuarioService = new UsuarioService();
    private final HabilidadeService habilidadeService = new HabilidadeService();
    private final TrocaService trocaService = new TrocaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario logado = usuario(request);
        if (logado == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        Long id = logado.getId();
        try { if (request.getParameter("id") != null) id = Long.parseLong(request.getParameter("id")); } catch (NumberFormatException ignored) { }
        Usuario perfil = usuarioService.buscarPorId(id);
        if (perfil == null) { response.sendRedirect(request.getContextPath() + "/feed"); return; }
        request.setAttribute("perfil", perfil);
        request.setAttribute("habilidades", habilidadeService.listarPorUsuario(id));
        request.setAttribute("trocas", trocaService.listar(id));
        request.getRequestDispatcher("/perfil.jsp").forward(request, response);
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
