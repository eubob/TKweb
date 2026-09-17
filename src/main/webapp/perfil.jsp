<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%@ page import="br.com.mvc.model.Habilidade" %>
<%@ page import="br.com.mvc.model.Troca" %>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) { response.sendRedirect("login.jsp"); return; }
    request.setAttribute("activePage", "perfil");
    Usuario perfil = (Usuario) request.getAttribute("perfil");
    List<Habilidade> habilidades = (List<Habilidade>) request.getAttribute("habilidades");
    List<Troca> trocas = (List<Troca>) request.getAttribute("trocas");
    boolean meuPerfil = perfil != null && perfil.getId().equals(logado.getId());
    String nome = perfil == null || perfil.getNome() == null ? "Usuário" : perfil.getNome().trim();
    String[] partes = nome.split("\\s+");
    String iniciais = partes.length > 1 ? (partes[0].substring(0,1) + partes[partes.length-1].substring(0,1)).toUpperCase() : (nome.length() > 1 ? nome.substring(0,2).toUpperCase() : nome.toUpperCase());
    String estrelas = "";
    int nota = (int) Math.round(perfil == null ? 0 : perfil.getMediaAvaliacao());
    for(int i=0;i<5;i++) estrelas += i < nota ? "★" : "☆";
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/social.css">
</head>
<body>
<div class="feed-shell">
    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>
    <main class="screen-column">
        <header class="screen-header">
            <div>
                <h1>Perfil</h1>
                <p><%= meuPerfil ? "Sua presença na comunidade." : "Conheça este usuário." %></p>
            </div>
        </header>

        <div class="screen-body-wide">
            <section class="section-card">
                <div class="profile-cover"></div>
                <div class="profile-content">
                    <div class="avatar profile-avatar"><%= iniciais %></div>
                    <h2 class="profile-name"><%= nome %></h2>
                    <div class="profile-email"><%= perfil.getEmail() %></div>
                    <div class="rating"><%= estrelas %> <span class="meta">(<%= perfil.getTotalAvaliacoes() %> avaliações)</span></div>

                    <div class="profile-stats">
                        <div class="profile-stat"><strong><%= perfil.getPontos() %></strong><span>pontos</span></div>
                        <div class="profile-stat"><strong><%= habilidades == null ? 0 : habilidades.size() %></strong><span>conhecimentos</span></div>
                        <div class="profile-stat"><strong><%= trocas == null ? 0 : trocas.size() %></strong><span>trocas</span></div>
                    </div>

                    <% if (meuPerfil) { %>
                        <div class="action-row"><a class="primary-button" href="<%= request.getContextPath() %>/habilidades">Gerenciar conhecimentos</a></div>
                    <% } %>
                </div>
            </section>

            <section class="section-card">
                <div class="section-card-inner">
                    <h2 class="section-title">Conhecimentos</h2>
                    <p class="section-subtitle">O que <%= meuPerfil ? "você" : nome %> oferece e deseja aprender.</p>
                </div>
                <% if (habilidades == null || habilidades.isEmpty()) { %>
                    <div class="empty-state">Nenhum conhecimento cadastrado.</div>
                <% } else { %>
                    <% for (Habilidade h : habilidades) { %>
                        <article class="knowledge-item">
                            <div class="knowledge-head">
                                <div class="avatar"><%= iniciais %></div>
                                <div class="knowledge-main">
                                    <h3 class="knowledge-title"><%= h.getTitulo() %></h3>
                                    <div class="tag-row"><span class="tag <%= "OFERECE".equals(h.getTipo()) ? "offer" : "want" %>"><%= "OFERECE".equals(h.getTipo()) ? "Oferece" : "Quer aprender" %></span><span class="tag"><%= h.getCategoria() %></span></div>
                                    <% if (h.getDescricao() != null && !h.getDescricao().isBlank()) { %><p class="knowledge-description"><%= h.getDescricao() %></p><% } %>
                                </div>
                            </div>
                        </article>
                    <% } %>
                <% } %>
            </section>
        </div>
    </main>
    <aside class="rightbar">
        <div class="search-box"><span>⌕</span><input type="text" placeholder="Pesquisar no feed"></div>
        <section class="panel">
            <div class="panel-title">Reputação</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Nota média</span><span class="panel-description"><%= String.format(java.util.Locale.US, "%.1f", perfil.getMediaAvaliacao()) %> / 5</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Avaliações</span><span class="panel-description"><%= perfil.getTotalAvaliacoes() %> experiências avaliadas.</span></span></div>
        </section>
    </aside>
</div>
</body>
</html>
