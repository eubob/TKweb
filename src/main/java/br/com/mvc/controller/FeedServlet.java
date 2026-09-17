package br.com.mvc.controller;

import br.com.mvc.model.Comentario;
import br.com.mvc.model.Habilidade;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.HabilidadeService;
import br.com.mvc.service.InteracaoService;
import br.com.mvc.service.TrocaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/feed")
public class FeedServlet extends HttpServlet {
    private final HabilidadeService habilidadeService = new HabilidadeService();
    private final InteracaoService interacaoService = new InteracaoService();
    private final TrocaService trocaService = new TrocaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String categoria = request.getParameter("categoria");
        String tipo = request.getParameter("tipo");
        List<Habilidade> feed = habilidadeService.listarFeed(categoria, tipo, usuario.getId());
        List<String> categorias = habilidadeService.listarCategorias();
        if (categorias.isEmpty()) categorias = List.of(HabilidadeService.CATEGORIAS);
        Map<String, Integer> categoriasPopulares = habilidadeService.listarCategoriasMaisPopulares();
        List<Habilidade> sugestoes = habilidadeService.listarSugestoes(usuario.getId(), 6);
        List<Habilidade> minhasOfertas = habilidadeService.listarPorUsuario(usuario.getId()).stream().filter(h -> "OFERECE".equals(h.getTipo())).toList();
        Map<Long, List<Comentario>> comentarios = new HashMap<>();
        for (Habilidade h : feed) comentarios.put(h.getId(), interacaoService.comentarios(h.getId()));
        request.setAttribute("feed", feed);
        request.setAttribute("categorias", categorias);
        request.setAttribute("categoriasPopulares", categoriasPopulares);
        request.setAttribute("sugestoes", sugestoes);
        request.setAttribute("minhasOfertas", minhasOfertas);
        request.setAttribute("comentarios", comentarios);
        request.getRequestDispatcher("/feed.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Usuario usuario = usuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String action = request.getParameter("action");
        try {
            Long habilidadeId = Long.parseLong(request.getParameter("habilidadeId"));
            if ("curtir".equals(action)) {
                interacaoService.alternarCurtida(habilidadeId, usuario.getId());
            } else if ("comentar".equals(action)) {
                interacaoService.comentar(habilidadeId, usuario.getId(), request.getParameter("texto"));
            } else if ("trocar".equals(action)) {
                Long habilidadeOferecidaId = Long.parseLong(request.getParameter("habilidadeOferecidaId"));
                trocaService.solicitar(usuario.getId(), habilidadeId, habilidadeOferecidaId);
            }
        } catch (NumberFormatException ignored) { }
        String categoria = request.getParameter("categoriaFiltro");
        String tipo = request.getParameter("tipoFiltro");
        String redirect = request.getContextPath() + "/feed";
        if (categoria != null && !categoria.isBlank()) redirect += "?categoria=" + java.net.URLEncoder.encode(categoria, java.nio.charset.StandardCharsets.UTF_8);
        if (tipo != null && !tipo.isBlank()) redirect += (redirect.contains("?") ? "&" : "?") + "tipo=" + java.net.URLEncoder.encode(tipo, java.nio.charset.StandardCharsets.UTF_8);
        response.sendRedirect(redirect);
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
