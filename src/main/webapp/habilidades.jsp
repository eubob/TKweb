<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="br.com.mvc.model.Habilidade" %>
<%@ page import="br.com.mvc.model.Usuario" %>

<%
    Usuario usuario =
            (Usuario) session.getAttribute("usuarioLogado");

    if (usuario == null) {

        response.sendRedirect("login.jsp");
        return;
    }

    List<Habilidade> habilidades =
            (List<Habilidade>) request.getAttribute("habilidades");

    Habilidade habilidadeEditar =
            (Habilidade) request.getAttribute("habilidadeEditar");

    String modo =
            (String) request.getAttribute("modo");

    boolean editar =
            "editar".equals(modo);

    String msgSucesso =
            (String) request.getAttribute("msgSucesso");

    String msgErro =
            (String) request.getAttribute("msgErro");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Troca de Conhecimentos</title>

    <link rel="stylesheet"
          href="css/habilidades.css">

</head>

<body>

<header class="topbar">

    <div class="topbar-content">

        <div>
            <h1>Troca de Conhecimentos</h1>

            <p>
                Olá,
                <strong><%= usuario.getNome() %></strong>!
            </p>
        </div>

        <div class="topbar-actions">

            <a href="home.jsp"
               class="btn btn-secondary">
                Início
            </a>

            <a href="auth?action=logout"
               class="btn btn-danger">
                Sair
            </a>

        </div>

    </div>

</header>


<main class="container">

    <% if (msgSucesso != null) { %>

        <div class="alert alert-success">
            <%= msgSucesso %>
        </div>

    <% } %>


    <% if (msgErro != null) { %>

        <div class="alert alert-error">
            <%= msgErro %>
        </div>

    <% } %>


    <section class="card">

        <div class="card-header">

            <div>

                <h2>
                    <%= editar
                            ? "Editar conhecimento"
                            : "Cadastrar conhecimento" %>
                </h2>

                <p>
                    Compartilhe algo que você sabe ou
                    algo que deseja aprender.
                </p>

            </div>

        </div>


        <form
                action="habilidades"
                method="post"
                class="knowledge-form">

            <input
                    type="hidden"
                    name="action"
                    value="<%= editar
                            ? "atualizar"
                            : "cadastrar" %>"
            >

            <% if (editar) { %>

                <input
                        type="hidden"
                        name="id"
                        value="<%= habilidadeEditar.getId() %>"
                >

            <% } %>


            <div class="form-group">

                <label for="titulo">
                    Título
                </label>

                <input
                        type="text"
                        id="titulo"
                        name="titulo"
                        maxlength="100"
                        required
                        placeholder="Ex.: Java, Excel, Inglês..."
                        value="<%= editar
                                ? habilidadeEditar.getTitulo()
                                : "" %>"
                >

            </div>


            <div class="form-group">

                <label for="tipo">
                    Tipo
                </label>

                <select
                        id="tipo"
                        name="tipo"
                        required>

                    <option
                            value="">
                        Selecione
                    </option>

                    <option
                            value="OFERECE"
                            <%= editar
                                    && "OFERECE".equals(
                                            habilidadeEditar.getTipo()
                                    )
                                    ? "selected"
                                    : "" %>>
                        Eu ofereço
                    </option>

                    <option
                            value="DESEJA"
                            <%= editar
                                    && "DESEJA".equals(
                                            habilidadeEditar.getTipo()
                                    )
                                    ? "selected"
                                    : "" %>>
                        Eu desejo aprender
                    </option>

                </select>

            </div>


            <div class="form-group">

                <label for="descricao">
                    Descrição
                </label>

                <textarea
                        id="descricao"
                        name="descricao"
                        rows="5"
                        placeholder="Explique o que você oferece ou deseja aprender..."
                ><%= editar && habilidadeEditar.getDescricao() != null
                        ? habilidadeEditar.getDescricao()
                        : "" %></textarea>

            </div>


            <div class="form-actions">

                <button
                        type="submit"
                        class="btn btn-primary">

                    <%= editar
                            ? "Atualizar"
                            : "Cadastrar" %>

                </button>


                <% if (editar) { %>

                    <a
                            href="habilidades"
                            class="btn btn-secondary">

                        Cancelar

                    </a>

                <% } %>

            </div>

        </form>

    </section>


    <section class="card">

        <div class="card-header">

            <div>

                <h2>
                    Meus conhecimentos
                </h2>

                <p>
                    Seus conhecimentos cadastrados.
                </p>

            </div>

        </div>


        <% if (habilidades == null
                || habilidades.isEmpty()) { %>

            <div class="empty-state">

                <h3>
                    Nenhum conhecimento cadastrado
                </h3>

                <p>
                    Cadastre seu primeiro conhecimento
                    usando o formulário acima.
                </p>

            </div>

        <% } else { %>

            <div class="knowledge-grid">

                <% for (Habilidade habilidade : habilidades) { %>

                    <article class="knowledge-card">

                        <div class="knowledge-card-header">

                            <h3>
                                <%= habilidade.getTitulo() %>
                            </h3>

                            <span
                                    class="badge
                                    <%= "OFERECE".equals(
                                            habilidade.getTipo()
                                    )
                                            ? "badge-oferece"
                                            : "badge-deseja" %>">

                                <%= "OFERECE".equals(
                                        habilidade.getTipo()
                                )
                                        ? "Ofereço"
                                        : "Desejo aprender" %>

                            </span>

                        </div>


                        <div class="knowledge-description">

                            <% if (habilidade.getDescricao() != null
                                    && !habilidade.getDescricao()
                                    .trim()
                                    .isEmpty()) { %>

                                <p>
                                    <%= habilidade.getDescricao() %>
                                </p>

                            <% } else { %>

                                <p class="empty-description">
                                    Sem descrição.
                                </p>

                            <% } %>

                        </div>


                        <div class="knowledge-actions">

                            <a
                                    href="habilidades?action=editar&id=<%= habilidade.getId() %>"
                                    class="btn btn-edit">

                                Editar

                            </a>


                            <form
                                    action="habilidades"
                                    method="post"
                                    onsubmit="return confirmarExclusao();">

                                <input
                                        type="hidden"
                                        name="action"
                                        value="excluir"
                                >

                                <input
                                        type="hidden"
                                        name="id"
                                        value="<%= habilidade.getId() %>"
                                >

                                <button
                                        type="submit"
                                        class="btn btn-danger">

                                    Excluir

                                </button>

                            </form>

                        </div>

                    </article>

                <% } %>

            </div>

        <% } %>

    </section>

</main>


<script>

    function confirmarExclusao() {

        return confirm(
            "Tem certeza que deseja excluir este conhecimento?"
        );
    }

</script>

</body>

</html>