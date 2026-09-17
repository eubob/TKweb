<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="br.com.mvc.model.Usuario" %>

<%
    Usuario usuario =
            (Usuario) session.getAttribute("usuarioLogado");

    if (usuario == null) {

        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>

<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Início - Troca de Conhecimentos</title>

    <link rel="stylesheet"
          href="css/style.css">

    <style>

        .home-container {
            width: 90%;
            max-width: 900px;
            margin: 50px auto;
        }

        .home-card {
            background: white;
            padding: 40px;
            border-radius: 12px;

            box-shadow:
                0 4px 15px rgba(0, 0, 0, 0.08);

            text-align: center;
        }

        .home-card h1 {
            margin-bottom: 15px;
        }

        .home-card p {
            color: #666;
            margin-bottom: 30px;
        }

        .home-actions {
            display: flex;
            justify-content: center;
            gap: 15px;
            flex-wrap: wrap;
        }

        .home-btn {
            display: inline-block;
            padding: 13px 22px;
            border-radius: 7px;
            text-decoration: none;
            font-weight: bold;
        }

        .home-btn-primary {
            background: #4A90E2;
            color: white;
        }

        .home-btn-secondary {
            background: #eeeeee;
            color: #333;
        }

    </style>

</head>

<body>

<div class="home-container">

    <div class="home-card">

        <h1>
            Bem-vindo, <%= usuario.getNome() %>!
        </h1>

        <p>
            Compartilhe conhecimentos e encontre
            pessoas interessadas em aprender ou ensinar.
        </p>

        <div class="home-actions">

            <a
                    href="habilidades"
                    class="home-btn home-btn-primary">

                Meus conhecimentos

            </a>

            <a
                    href="auth?action=logout"
                    class="home-btn home-btn-secondary">

                Sair

            </a>

        </div>

    </div>

</div>

</body>

</html>