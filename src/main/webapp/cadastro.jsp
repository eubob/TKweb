<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastro - TKWeb</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-container">
        <h2>Criar Conta</h2>

        <% if (request.getAttribute("msgErro") != null) { %>
            <div class="alert-error"><%= request.getAttribute("msgErro") %></div>
        <% } %>

        <form action="auth" method="post">
            <input type="hidden" name="action" value="cadastrar">
            
            <div class="form-group">
                <label for="nome">Nome Completo</label>
                <input type="text" id="nome" name="nome" required placeholder="Seu Nome">
            </div>

            <div class="form-group">
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" required placeholder="seu@email.com">
            </div>
            
            <div class="form-group">
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" required placeholder="••••••••">
            </div>
            
            <button type="submit" class="btn-submit">Cadastrar</button>
        </form>

        <div class="auth-link">
            Já possui conta? <a href="login.jsp">Faça Login</a>
        </div>
    </div>
</body>
</html>