package br.com.mvc.controller;

import br.com.mvc.model.Troca;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.ChatService;
import br.com.mvc.service.TrocaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private final ChatService chatService = new ChatService();
    private final TrocaService trocaService = new TrocaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        try {
            Long trocaId = Long.parseLong(request.getParameter("trocaId"));
            Troca troca = trocaService.buscar(trocaId);
            if (troca == null || !trocaService.participa(trocaId, usuario.getId())) { response.sendRedirect(request.getContextPath() + "/trocas"); return; }
            request.setAttribute("troca", troca);
            request.setAttribute("mensagens", chatService.listar(trocaId, usuario.getId()));
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        } catch (NumberFormatException e) { response.sendRedirect(request.getContextPath() + "/trocas"); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Usuario usuario = usuario(request);
        if (usuario == null) { response.sendRedirect(request.getContextPath() + "/login.jsp"); return; }
        try {
            Long trocaId = Long.parseLong(request.getParameter("trocaId"));
            chatService.enviar(trocaId, usuario.getId(), request.getParameter("mensagem"));
            response.sendRedirect(request.getContextPath() + "/chat?trocaId=" + trocaId);
        } catch (NumberFormatException e) { response.sendRedirect(request.getContextPath() + "/trocas"); }
    }

    private Usuario usuario(HttpServletRequest request) {
        Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("usuarioLogado");
        return value instanceof Usuario ? (Usuario) value : null;
    }
}
