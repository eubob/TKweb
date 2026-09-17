<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Troca" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null) { response.sendRedirect("login.jsp"); return; }
    request.setAttribute("activePage", "trocas");
    List<Troca> trocas = (List<Troca>) request.getAttribute("trocas");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Minhas trocas</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/social.css">
</head>
<body>
<div class="feed-shell">
    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>
    <main class="screen-column">
        <header class="screen-header">
            <div>
                <h1>Trocas</h1>
                <p>Acompanhe solicitações, conversas e trocas concluídas.</p>
            </div>
        </header>
        <div class="screen-body">
            <section class="section-card">
                <% if (trocas == null || trocas.isEmpty()) { %>
                    <div class="empty-state">Você ainda não possui trocas. Explore o feed para encontrar alguém compatível.</div>
                <% } else { %>
                    <div class="swap-list">
                    <% for (Troca t : trocas) {
                        boolean souSolicitante = usuario.getId().equals(t.getSolicitanteId());
                        String outraPessoa = souSolicitante ? t.getReceptorNome() : t.getSolicitanteNome();
                        boolean possoDecidir = !souSolicitante && "PENDENTE".equals(t.getStatus());
                    %>
                        <article class="swap-item">
                            <div class="swap-head">
                                <div class="avatar"><%= outraPessoa == null || outraPessoa.isBlank() ? "TK" : outraPessoa.substring(0,1).toUpperCase() %></div>
                                <div class="swap-main">
                                    <div class="meta">Troca com <strong><%= outraPessoa %></strong></div>
                                    <span class="status-pill status-<%= t.getStatus() %>"><%= t.getStatus() %></span>
                                    <div class="meta" style="margin-top:6px;"><%= t.getAtualizadaEm() == null ? "Agora" : t.getAtualizadaEm() %></div>
                                </div>
                            </div>

                            <div class="swap-flow">
                                <div class="swap-box">
                                    <div class="meta">Você / seu conhecimento</div>
                                    <h3 class="swap-title"><%= souSolicitante ? t.getHabilidadeOferecidaTitulo() : t.getHabilidadeSolicitadaTitulo() %></h3>
                                    <p class="swap-description"><%= souSolicitante ? "Você ofereceu este conhecimento." : "Você receberia este conhecimento." %></p>
                                </div>
                                <div class="swap-arrow">↔</div>
                                <div class="swap-box">
                                    <div class="meta"><%= souSolicitante ? "Conhecimento solicitado" : "Conhecimento oferecido pelo outro usuário" %></div>
                                    <h3 class="swap-title"><%= souSolicitante ? t.getHabilidadeSolicitadaTitulo() : t.getHabilidadeOferecidaTitulo() %></h3>
                                    <p class="swap-description"><%= souSolicitante ? "A pessoa ensina este conhecimento." : "A pessoa oferece este conhecimento." %></p>
                                </div>
                            </div>

                            <div class="action-row">
                                <% if (possoDecidir) { %>
                                    <form class="inline-form" action="<%= request.getContextPath() %>/trocas" method="post">
                                        <input type="hidden" name="trocaId" value="<%= t.getId() %>">
                                        <input type="hidden" name="action" value="aceitar">
                                        <button class="primary-button" type="submit">Aceitar</button>
                                    </form>
                                    <form class="inline-form" action="<%= request.getContextPath() %>/trocas" method="post">
                                        <input type="hidden" name="trocaId" value="<%= t.getId() %>">
                                        <input type="hidden" name="action" value="recusar">
                                        <button class="danger-button" type="submit">Recusar</button>
                                    </form>
                                <% } %>

                                <% if ("ACEITA".equals(t.getStatus()) || "FINALIZADA".equals(t.getStatus())) { %>
                                    <a class="primary-button" href="<%= request.getContextPath() %>/chat?trocaId=<%= t.getId() %>">Abrir chat</a>
                                <% } %>

                                <% if ("ACEITA".equals(t.getStatus())) { %>
                                    <form class="inline-form" action="<%= request.getContextPath() %>/trocas" method="post">
                                        <input type="hidden" name="trocaId" value="<%= t.getId() %>">
                                        <input type="hidden" name="action" value="finalizar">
                                        <button class="ghost-button" type="submit">Finalizar troca</button>
                                    </form>
                                <% } %>
                            </div>

                            <% if ("FINALIZADA".equals(t.getStatus()) && !t.isAvaliacaoEnviada()) { %>
                                <form class="section-card" action="<%= request.getContextPath() %>/avaliar" method="post" style="margin-top:14px;margin-bottom:0;background:#0c0d0e;">
                                    <div class="section-card-inner">
                                        <input type="hidden" name="trocaId" value="<%= t.getId() %>">
                                        <h3 class="section-title" style="font-size:16px;">Avalie esta troca</h3>
                                        <p class="section-subtitle">Sua avaliação ajuda a construir a reputação da comunidade.</p>
                                        <div class="form-grid" style="margin-top:12px;">
                                            <div>
                                                <label class="field-label" for="nota-<%= t.getId() %>">Nota</label>
                                                <select class="field-select" id="nota-<%= t.getId() %>" name="nota" required>
                                                    <option value="5">★★★★★ 5</option>
                                                    <option value="4">★★★★☆ 4</option>
                                                    <option value="3">★★★☆☆ 3</option>
                                                    <option value="2">★★☆☆☆ 2</option>
                                                    <option value="1">★☆☆☆☆ 1</option>
                                                </select>
                                            </div>
                                            <div>
                                                <label class="field-label" for="comentario-<%= t.getId() %>">Comentário</label>
                                                <textarea class="field-textarea" id="comentario-<%= t.getId() %>" name="comentario" maxlength="500" style="min-height:90px;" placeholder="Como foi a experiência?"></textarea>
                                            </div>
                                            <button class="primary-button" type="submit">Enviar avaliação</button>
                                        </div>
                                    </div>
                                </form>
                            <% } else if (t.isAvaliacaoEnviada()) { %>
                                <div class="notice success" style="margin-top:14px;margin-bottom:0;">Avaliação enviada.</div>
                            <% } %>
                        </article>
                    <% } %>
                    </div>
                <% } %>
            </section>
        </div>
    </main>
    <aside class="rightbar">
        <div class="search-box"><span>⌕</span><input type="text" placeholder="Buscar pessoas"></div>
        <section class="panel">
            <div class="panel-title">Status das trocas</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Pendente</span><span class="panel-description">Aguardando resposta do outro usuário.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Aceita</span><span class="panel-description">Chat liberado para combinar a sessão.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Finalizada</span><span class="panel-description">Hora de avaliar a experiência.</span></span></div>
        </section>
    </aside>
</div>
</body>
</html>
