<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Habilidade" %>
<%@ page import="br.com.mvc.model.Usuario" %>
<%@ page import="br.com.mvc.service.HabilidadeService" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    if (usuario == null) { response.sendRedirect("login.jsp"); return; }
    request.setAttribute("activePage", "habilidades");
    List<Habilidade> habilidades = (List<Habilidade>) request.getAttribute("habilidades");
    Habilidade habilidadeEditar = (Habilidade) request.getAttribute("habilidadeEditar");
    boolean editar = "editar".equals(request.getAttribute("modo")) && habilidadeEditar != null;
    String msg = request.getParameter("msg");
    String nome = usuario.getNome() == null ? "Usuário" : usuario.getNome().trim();
    String[] partes = nome.split("\\s+");
    String iniciais = partes.length > 1 ? (partes[0].substring(0,1) + partes[partes.length-1].substring(0,1)).toUpperCase() : (nome.length() > 1 ? nome.substring(0,2).toUpperCase() : nome.toUpperCase());
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Meus conhecimentos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/feed.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/social.css">
</head>
<body>
<div class="feed-shell">
    <%@ include file="/WEB-INF/includes/sidebar.jspf" %>

    <main class="screen-column">
        <header class="screen-header">
            <div>
                <h1>Meus conhecimentos</h1>
                <p>O que você ensina e o que você quer aprender.</p>
            </div>
        </header>

        <div class="screen-body">
            <% if ("criado".equals(msg)) { %><div class="notice success">Conhecimento publicado com sucesso.</div><% } %>
            <% if ("atualizado".equals(msg)) { %><div class="notice success">Conhecimento atualizado com sucesso.</div><% } %>
            <% if ("excluido".equals(msg)) { %><div class="notice success">Conhecimento excluído.</div><% } %>
            <% if ("erro".equals(msg)) { %><div class="notice error">Não foi possível concluir a operação.</div><% } %>

            <section class="section-card">
                <div class="section-card-inner">
                    <h2 class="section-title"><%= editar ? "Editar conhecimento" : "Criar publicação" %></h2>
                    <p class="section-subtitle">Compartilhe uma habilidade que você oferece ou um conhecimento que deseja aprender.</p>

                    <form class="form-grid" action="<%= request.getContextPath() %>/habilidades" method="post" style="margin-top:16px;">
                        <input type="hidden" name="action" value="<%= editar ? "atualizar" : "cadastrar" %>">
                        <% if (editar) { %><input type="hidden" name="id" value="<%= habilidadeEditar.getId() %>"><% } %>

                        <div>
                            <label class="field-label" for="titulo">Título</label>
                            <input class="field-input" id="titulo" name="titulo" maxlength="100" required placeholder="Ex.: Python para iniciantes" value="<%= editar && habilidadeEditar.getTitulo() != null ? habilidadeEditar.getTitulo() : "" %>">
                        </div>

                        <div>
                            <label class="field-label" for="tipo">Tipo</label>
                            <select class="field-select" id="tipo" name="tipo" required>
                                <option value="">Selecione</option>
                                <option value="OFERECE" <%= editar && "OFERECE".equals(habilidadeEditar.getTipo()) ? "selected" : "" %>>Eu ofereço</option>
                                <option value="DESEJA" <%= editar && "DESEJA".equals(habilidadeEditar.getTipo()) ? "selected" : "" %>>Eu quero aprender</option>
                            </select>
                        </div>

                        <div>
                            <label class="field-label" for="categoria">Categoria</label>
                            <select class="field-select" id="categoria" name="categoria" required>
                                <option value="">Selecione</option>
                                <% for (String categoria : HabilidadeService.CATEGORIAS) { %>
                                    <option value="<%= categoria %>" <%= editar && categoria.equals(habilidadeEditar.getCategoria()) ? "selected" : "" %>><%= categoria %></option>
                                <% } %>
                            </select>
                        </div>

                        <div>
                            <label class="field-label" for="descricao">Descrição</label>
                            <textarea class="field-textarea" id="descricao" name="descricao" maxlength="1000" placeholder="Conte um pouco mais sobre o conhecimento."><%= editar && habilidadeEditar.getDescricao() != null ? habilidadeEditar.getDescricao() : "" %></textarea>
                        </div>

                        <div class="action-row">
                            <button class="primary-button" type="submit"><%= editar ? "Salvar alterações" : "Publicar" %></button>
                            <% if (editar) { %><a class="ghost-button" href="<%= request.getContextPath() %>/habilidades">Cancelar</a><% } %>
                        </div>
                    </form>
                </div>
            </section>

            <section class="section-card">
                <div class="section-card-inner">
                    <h2 class="section-title">Suas publicações</h2>
                    <p class="section-subtitle">Gerencie os conhecimentos que aparecem no seu perfil e nas recomendações.</p>
                </div>

                <% if (habilidades == null || habilidades.isEmpty()) { %>
                    <div class="empty-state">Você ainda não publicou nenhum conhecimento.</div>
                <% } else { %>
                    <% for (Habilidade h : habilidades) { %>
                        <article class="knowledge-item">
                            <div class="knowledge-head">
                                <div class="avatar"><%= iniciais %></div>
                                <div class="knowledge-main">
                                    <h3 class="knowledge-title"><%= h.getTitulo() %></h3>
                                    <div class="meta"><%= h.getCriadoEm() == null ? "Agora" : h.getCriadoEm() %></div>
                                    <div class="tag-row">
                                        <span class="tag <%= "OFERECE".equals(h.getTipo()) ? "offer" : "want" %>"><%= "OFERECE".equals(h.getTipo()) ? "Oferece" : "Quer aprender" %></span>
                                        <span class="tag"><%= h.getCategoria() %></span>
                                    </div>
                                    <% if (h.getDescricao() != null && !h.getDescricao().isBlank()) { %>
                                        <p class="knowledge-description"><%= h.getDescricao() %></p>
                                    <% } %>
                                    <div class="action-row">
                                        <a class="ghost-button" href="<%= request.getContextPath() %>/habilidades?action=editar&id=<%= h.getId() %>">Editar</a>
                                        <form class="inline-form" action="<%= request.getContextPath() %>/habilidades" method="post" onsubmit="return confirm('Excluir este conhecimento?');">
                                            <input type="hidden" name="action" value="excluir">
                                            <input type="hidden" name="id" value="<%= h.getId() %>">
                                            <button class="danger-button" type="submit">Excluir</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </article>
                    <% } %>
                <% } %>
            </section>
        </div>
    </main>

    <aside class="rightbar">
        <div class="search-box"><span>⌕</span><input type="text" placeholder="Buscar no feed" onkeydown="if(event.key==='Enter'){window.location.href='<%= request.getContextPath() %>/feed';}"></div>
        <section class="panel">
            <div class="panel-title">Como funciona</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Ofereça</span><span class="panel-description">Publique algo que você sabe ensinar.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Aprenda</span><span class="panel-description">Cadastre o que você quer aprender.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name">Troque</span><span class="panel-description">Encontre alguém com interesse complementar.</span></span></div>
        </section>
        <section class="panel">
            <div class="panel-title">Seu resumo</div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name"><%= habilidades == null ? 0 : habilidades.size() %> conhecimentos</span><span class="panel-description">Publicações no seu perfil.</span></span></div>
            <div class="panel-row"><span class="panel-content"><span class="panel-name"><%= usuario.getPontos() %> pontos</span><span class="panel-description">Por ensinar, aprender e interagir.</span></span></div>
        </section>
    </aside>
</div>
</body>
</html>
