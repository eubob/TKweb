<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Notificacao" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null) { response.sendRedirect("login.jsp"); return; }
    request.setAttribute("activePage", "notificacoes");
    List<Notificacao> notificacoes = (List<Notificacao>) request.getAttribute("notificacoes");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Notificações</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/social.css">
</head>
<body>
<div class="feed-shell">
    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>
    <main class="screen-column">
        <header class="screen-header">
            <div>
                <h1>Notificações</h1>
                <p>Veja o que aconteceu com suas publicações e trocas.</p>
            </div>
        </header>
        <div class="screen-body">
            <section class="section-card">
                <% if (notificacoes == null || notificacoes.isEmpty()) { %>
                    <div class="empty-state">Você está em dia. Nenhuma notificação por enquanto.</div>
                <% } else { %>
                    <% for (Notificacao n : notificacoes) {
                        String icone = "RECOMENDACAO".equals(n.getTipo()) ? "✦" : "TROCA".equals(n.getTipo()) ? "↔" : "•";
                    %>
                        <article class="notification-item">
                            <div class="notification-icon"><%= icone %></div>
                            <div class="notification-main">
                                <a class="notification-message" href="<%= request.getContextPath() %>/<%= n.getUrl() == null ? "feed" : n.getUrl() %>"><%= n.getMensagem() %></a>
                                <div class="notification-time"><%= n.getCriadaEm() == null ? "Agora" : n.getCriadaEm() %></div>
                            </div>
                        </article>
                    <% } %>
                <% } %>
            </section>
        </div>
    </main>
    <aside class="rightbar">
        <div class="search-box"><span>⌕</span><input type="text" placeholder="Pesquisar"></div>
        <section class="panel">
            <div class="panel-title">Atividade</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Trocas</span><span class="panel-description">Solicitações, aceitações e finalizações aparecem aqui.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Recomendações</span><span class="panel-description">Novos conhecimentos compatíveis com seus interesses.</span></span></div>
        </section>
    </aside>
</div>
</body>
</html>
