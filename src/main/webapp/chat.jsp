<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Troca" %>
<%@ page import="br.com.mvc.model.Mensagem" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null) { response.sendRedirect("login.jsp"); return; }
    request.setAttribute("activePage", "trocas");
    Troca troca = (Troca) request.getAttribute("troca");
    List<Mensagem> mensagens = (List<Mensagem>) request.getAttribute("mensagens");
    String outraPessoa = usuario.getId().equals(troca.getSolicitanteId()) ? troca.getReceptorNome() : troca.getSolicitanteNome();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat da troca</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/social.css">
</head>
<body>
<div class="feed-shell">
    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>
    <main class="screen-column">
        <header class="screen-header">
            <a class="ghost-button" href="<%= request.getContextPath() %>/trocas">←</a>
            <div>
                <h1><%= outraPessoa %></h1>
                <p><%= troca.getHabilidadeOferecidaTitulo() %> ↔ <%= troca.getHabilidadeSolicitadaTitulo() %></p>
            </div>
        </header>
        <div class="chat-page">
            <div class="chat-messages" id="chatMessages">
                <% if (mensagens == null || mensagens.isEmpty()) { %>
                    <div class="empty-state">Comece a conversa e combine os detalhes da troca.</div>
                <% } else { %>
                    <% for (Mensagem m : mensagens) { boolean minha = usuario.getId().equals(m.getRemetenteId()); %>
                        <div class="message-row <%= minha ? "mine" : "" %>">
                            <div class="message-bubble">
                                <div class="message-author"><%= minha ? "Você" : m.getRemetenteNome() %></div>
                                <div><%= m.getMensagem() %></div>
                                <div class="message-time"><%= m.getEnviadaEm() == null ? "Agora" : m.getEnviadaEm() %></div>
                            </div>
                        </div>
                    <% } %>
                <% } %>
            </div>

            <% if ("ACEITA".equals(troca.getStatus()) || "FINALIZADA".equals(troca.getStatus())) { %>
                <div class="chat-compose">
                    <form action="<%= request.getContextPath() %>/chat" method="post">
                        <input type="hidden" name="trocaId" value="<%= troca.getId() %>">
                        <input name="mensagem" maxlength="1000" required autocomplete="off" placeholder="Escreva uma mensagem..."><button class="primary-button" type="submit">Enviar</button>
                    </form>
                </div>
            <% } else { %>
                <div class="notice" style="margin:12px 16px;">O chat será liberado quando a troca for aceita.</div>
            <% } %>
        </div>
    </main>
    <aside class="rightbar">
        <div class="search-box"><span>⌕</span><input type="text" placeholder="Pesquisar"></div>
        <section class="panel">
            <div class="panel-title">Resumo da troca</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Você oferece</span><span class="panel-description"><%= troca.getHabilidadeOferecidaTitulo() %></span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Você aprende</span><span class="panel-description"><%= troca.getHabilidadeSolicitadaTitulo() %></span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Status</span><span class="panel-description"><%= troca.getStatus() %></span></span></div>
        </section>
    </aside>
</div>
<script>
window.addEventListener('load', function(){
    const box = document.getElementById('chatMessages');
    if (box) box.scrollTop = box.scrollHeight;
});
</script>
</body>
</html>
