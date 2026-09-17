<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%@ page import="br.com.mvc.model.Habilidade" %>
<%@ page import="br.com.mvc.model.Comentario" %>

<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Habilidade> feed = (List<Habilidade>) request.getAttribute("feed");
    List<String> categorias = (List<String>) request.getAttribute("categorias");
    List<Habilidade> sugestoes = (List<Habilidade>) request.getAttribute("sugestoes");
    List<Habilidade> minhasOfertas = (List<Habilidade>) request.getAttribute("minhasOfertas");
    Map<Long, List<Comentario>> comentarios = (Map<Long, List<Comentario>>) request.getAttribute("comentarios");
    Map<String, Integer> categoriasPopulares = (Map<String, Integer>) request.getAttribute("categoriasPopulares");
    request.setAttribute("activePage", "feed");

    String categoriaAtual = request.getParameter("categoria");
    String tipoAtual = request.getParameter("tipo");

    String nome = usuario.getNome() == null ? "Usuário" : usuario.getNome().trim();
    String[] partes = nome.split("\\s+");
    String iniciais = partes.length == 1
            ? partes[0].substring(0, Math.min(2, partes[0].length())).toUpperCase()
            : (partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)).toUpperCase();
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Feed - Troca de Conhecimentos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
</head>
<body>
<div class="feed-shell">

    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>

    <main class="timeline">
        <header class="timeline-header">
            <span class="timeline-title">Para você</span>
        </header>

        <section class="composer">
            <div class="composer-row">
                <span class="avatar large"><%= iniciais %></span>
                <textarea class="composer-input" placeholder="O que você quer ensinar ou aprender?" readonly onclick="window.location.href='<%= request.getContextPath() %>/habilidades'"></textarea>
            </div>
            <div class="composer-help">Crie uma oferta ou pedido de conhecimento para aparecer no feed.</div>
            <div class="composer-actions">
                <a class="composer-submit" href="<%= request.getContextPath() %>/habilidades">Publicar</a>
            </div>
        </section>

        <div class="filterbar">
            <a class="filter-link <%= categoriaAtual == null ? "active" : "" %>" href="<%= request.getContextPath() %>/feed">Todos</a>
            <% if (categorias != null) { for (String categoria : categorias) { %>
                <a class="filter-link <%= categoria.equals(categoriaAtual) ? "active" : "" %>"
                   href="<%= request.getContextPath() %>/feed?categoria=<%= java.net.URLEncoder.encode(categoria, java.nio.charset.StandardCharsets.UTF_8) %>">
                    <%= categoria %>
                </a>
            <% }} %>
        </div>

        <% if (feed == null || feed.isEmpty()) { %>
            <div class="empty">
                Ainda não há publicações para os filtros selecionados.
            </div>
        <% } else { %>
            <% for (Habilidade h : feed) {
                String autor = h.getUsuarioNome() == null ? "Usuário" : h.getUsuarioNome();
                String[] p = autor.trim().split("\\s+");
                String ai = p.length == 1
                        ? p[0].substring(0, Math.min(2, p[0].length())).toUpperCase()
                        : (p[0].substring(0, 1) + p[p.length - 1].substring(0, 1)).toUpperCase();
                boolean minha = usuario.getId().equals(h.getUsuarioId());
                List<Comentario> listaComentarios = comentarios == null ? null : comentarios.get(h.getId());
            %>
                <article class="post">
                    <a class="avatar" href="<%= request.getContextPath() %>/perfil?id=<%= h.getUsuarioId() %>"><%= ai %></a>

                    <div class="post-body">
                        <div class="post-topline">
                            <a class="post-name" href="<%= request.getContextPath() %>/perfil?id=<%= h.getUsuarioId() %>"><%= autor %></a>
                            <span class="post-dot">·</span>
                            <span class="post-time"><%= h.getCriadoEm() == null ? "agora" : h.getCriadoEm() %></span>
                        </div>

                        <div class="post-kind"><%= "OFERECE".equals(h.getTipo()) ? "Oferece conhecimento" : "Busca conhecimento" %> · <%= h.getCategoria() %></div>
                        <h2 class="post-title"><%= h.getTitulo() %></h2>

                        <% if (h.getDescricao() != null && !h.getDescricao().isBlank()) { %>
                            <p class="post-description"><%= h.getDescricao() %></p>
                        <% } %>

                        <div class="post-actions">
                            <form method="post" action="<%= request.getContextPath() %>/feed">
                                <input type="hidden" name="action" value="comentar">
                                <input type="hidden" name="habilidadeId" value="<%= h.getId() %>">
                                <input type="hidden" name="categoriaFiltro" value="<%= categoriaAtual == null ? "" : categoriaAtual %>">
                                <input type="hidden" name="tipoFiltro" value="<%= tipoAtual == null ? "" : tipoAtual %>">
                                <button class="icon-button" type="button" onclick="document.getElementById('comment-<%= h.getId() %>').focus()">
                                    <span class="icon">💬</span>
                                    <span><%= h.getComentarios() %></span>
                                </button>
                            </form>

                            <form method="post" action="<%= request.getContextPath() %>/feed">
                                <input type="hidden" name="action" value="curtir">
                                <input type="hidden" name="habilidadeId" value="<%= h.getId() %>">
                                <input type="hidden" name="categoriaFiltro" value="<%= categoriaAtual == null ? "" : categoriaAtual %>">
                                <input type="hidden" name="tipoFiltro" value="<%= tipoAtual == null ? "" : tipoAtual %>">
                                <button class="icon-button <%= h.isCurtidaPeloUsuario() ? "liked" : "" %>" type="submit">
                                    <span class="icon"><%= h.isCurtidaPeloUsuario() ? "♥" : "♡" %></span>
                                    <span><%= h.getCurtidas() %></span>
                                </button>
                            </form>

                            <% if (!minha) { %>
                                <button class="icon-button" type="button" onclick="document.getElementById('exchange-<%= h.getId() %>').scrollIntoView({behavior:'smooth', block:'center'})">
                                    <span class="icon">↔</span>
                                    <span>Trocar</span>
                                </button>
                            <% } %>

                            <a class="icon-button" href="<%= request.getContextPath() %>/perfil?id=<%= h.getUsuarioId() %>">
                                <span class="icon">◉</span>
                            </a>
                        </div>

                        <% if (!minha && minhasOfertas != null && !minhasOfertas.isEmpty()) { %>
                            <div class="exchange-box" id="exchange-<%= h.getId() %>">
                                <div class="exchange-title">Propor troca com <%= autor %></div>
                                <form class="exchange-form" method="post" action="<%= request.getContextPath() %>/feed">
                                    <input type="hidden" name="action" value="trocar">
                                    <input type="hidden" name="habilidadeId" value="<%= h.getId() %>">
                                    <input type="hidden" name="categoriaFiltro" value="<%= categoriaAtual == null ? "" : categoriaAtual %>">
                                    <input type="hidden" name="tipoFiltro" value="<%= tipoAtual == null ? "" : tipoAtual %>">
                                    <select name="habilidadeOferecidaId" required>
                                        <option value="">Escolha o que você oferece</option>
                                        <% for (Habilidade oferta : minhasOfertas) { %>
                                            <option value="<%= oferta.getId() %>"><%= oferta.getTitulo() %></option>
                                        <% } %>
                                    </select>
                                    <button type="submit">Solicitar</button>
                                </form>
                            </div>
                        <% } %>

                        <% if (listaComentarios != null && !listaComentarios.isEmpty()) { %>
                            <div class="comments">
                                <% for (Comentario c : listaComentarios) { %>
                                    <div class="comment">
                                        <div class="comment-head">
                                            <span class="comment-name"><%= c.getUsuarioNome() %></span>
                                            <span class="comment-time">· <%= c.getCriadoEm() == null ? "agora" : c.getCriadoEm() %></span>
                                        </div>
                                        <p class="comment-text"><%= c.getTexto() %></p>
                                    </div>
                                <% } %>
                            </div>
                        <% } %>

                        <form class="comment-form" method="post" action="<%= request.getContextPath() %>/feed">
                            <input type="hidden" name="action" value="comentar">
                            <input type="hidden" name="habilidadeId" value="<%= h.getId() %>">
                            <input type="hidden" name="categoriaFiltro" value="<%= categoriaAtual == null ? "" : categoriaAtual %>">
                            <input type="hidden" name="tipoFiltro" value="<%= tipoAtual == null ? "" : tipoAtual %>">
                            <input id="comment-<%= h.getId() %>" name="texto" maxlength="500" placeholder="Responder à publicação..." required>
                            <button type="submit">Enviar</button>
                        </form>
                    </div>
                </article>
            <% } %>
        <% } %>
    </main>

    <aside class="rightbar">
        <div class="search-box">
            <span>⌕</span>
            <input type="text" placeholder="Buscar no feed" oninput="filtrarFeed(this.value)">
        </div>

        <section class="panel">
            <div class="panel-title">Conhecimentos para você</div>
            <% if (sugestoes == null || sugestoes.isEmpty()) { %>
                <div class="empty">Crie interesses no seu perfil publicando conhecimentos que você deseja aprender para receber sugestões.</div>
            <% } else { %>
                <% for (Habilidade sugestao : sugestoes) { %>
                    <a class="panel-row" href="<%= request.getContextPath() %>/perfil?id=<%= sugestao.getUsuarioId() %>">
                        <span class="avatar"><%= sugestao.getUsuarioNome() == null ? "TK" : sugestao.getUsuarioNome().substring(0, 1).toUpperCase() %></span>
                        <span class="panel-content">
                            <span class="panel-label"><%= sugestao.getCategoria() %></span>
                            <span class="panel-name"><%= sugestao.getTitulo() %></span>
                            <span class="panel-description">Oferecido por <%= sugestao.getUsuarioNome() %></span>
                        </span>
                        <span class="panel-action">Ver</span>
                    </a>
                <% } %>
            <% } %>
        </section>

        <section class="panel">
            <div class="panel-title">Categorias</div>
            <% if (categoriasPopulares != null && !categoriasPopulares.isEmpty()) { %>
                <% for (Map.Entry<String, Integer> categoria : categoriasPopulares.entrySet()) { %>
                    <a class="panel-row" href="<%= request.getContextPath() %>/feed?categoria=<%= java.net.URLEncoder.encode(categoria.getKey(), java.nio.charset.StandardCharsets.UTF_8) %>">
                        <span class="panel-content">
                            <span class="panel-name"><%= categoria.getKey() %></span>
                            <span class="panel-description"><%= categoria.getValue() %> publicações</span>
                        </span>
                        <span class="panel-action">→</span>
                    </a>
                <% } %>
            <% } %>
        </section>

        <div style="color:#71767b;font-size:12px;padding:0 8px 20px;">
            Troca de Conhecimentos · Compartilhe o que sabe. Aprenda o que procura.
            
        </div>
    </aside>
</div>

<script>
function filtrarFeed(value) {
    const termo = value.toLowerCase().trim();
    document.querySelectorAll('.post').forEach(post => {
        post.style.display = post.innerText.toLowerCase().includes(termo) ? '' : 'none';
    });
}
</script>
</body>
</html>
