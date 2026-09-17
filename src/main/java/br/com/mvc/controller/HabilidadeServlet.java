package br.com.mvc.controller;

import br.com.mvc.model.Habilidade;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.HabilidadeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/habilidades")
public class HabilidadeServlet extends HttpServlet {
    private final HabilidadeService service = new HabilidadeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = obterUsuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String action = request.getParameter("action");
        if ("editar".equals(action)) {
            try {
                Long id = Long.parseLong(request.getParameter("id"));
                request.setAttribute("habilidadeEditar", service.buscarPorId(id, usuario.getId()));
                request.setAttribute("modo", "editar");
            } catch (NumberFormatException ignored) {
                request.setAttribute("msgErro", "Conhecimento inválido.");
            }
        }
        request.setAttribute("habilidades", service.listarPorUsuario(usuario.getId()));
        request.setAttribute("categorias", HabilidadeService.CATEGORIAS);
        request.getRequestDispatcher("/habilidades.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Usuario usuario = obterUsuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String action = request.getParameter("action");
        boolean ok = false;
        String mensagem = "erro";
        try {
            if ("cadastrar".equals(action)) {
                ok = service.cadastrar(new Habilidade(null, request.getParameter("titulo"), request.getParameter("descricao"), request.getParameter("tipo"), request.getParameter("categoria"), usuario.getId()));
                mensagem = "criado";
            } else if ("atualizar".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                ok = service.atualizar(new Habilidade(id, request.getParameter("titulo"), request.getParameter("descricao"), request.getParameter("tipo"), request.getParameter("categoria"), usuario.getId()));
                mensagem = "atualizado";
            } else if ("excluir".equals(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                ok = service.excluir(id, usuario.getId());
                mensagem = "excluido";
            }
        } catch (NumberFormatException ignored) { }
        response.sendRedirect(request.getContextPath() + "/habilidades?msg=" + (ok ? mensagem : "erro"));
    }

    private Usuario obterUsuario(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
