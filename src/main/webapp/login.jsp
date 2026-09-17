<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Login - TKWeb</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <h2>TKWEB</h2>

        <% if (request.getAttribute("msgErro") != null) { %>
            <div class="alert-error"><%= request.getAttribute("msgErro") %></div>
        <% } %>

        <% if (request.getAttribute("msgSucesso") != null) { %>
            <div class="alert-success"><%= request.getAttribute("msgSucesso") %></div>
        <% } %>

        <form action="auth" method="post">
            <input type="hidden" name="action" value="login">
            
            <div class="form-group">
                <label for="email">E-mail</label>
                <input
                    type="email"
                    id="email"
                    name="email"
                    required
                    autocomplete="email"
                    placeholder="seu@email.com">
            </div>

            <div class="form-group">
                <label for="senha">Senha</label>
                <input
                    type="password"
                    id="senha"
                    name="senha"
                    required
                    autocomplete="current-password"
                    placeholder="Digite sua senha">
            </div>

            
            <button type="submit" class="btn-submit">Entrar</button>
        </form>

        <div class="auth-link">
            Não tem uma conta? <a href="cadastro.jsp">Cadastre-se</a>
        </div>
    </div>
</body>
</html>