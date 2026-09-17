package br.com.mvc.controller;

import br.com.mvc.model.Habilidade;
import br.com.mvc.model.Usuario;
import br.com.mvc.service.HabilidadeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/habilidades")
public class HabilidadeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final HabilidadeService habilidadeService =
            new HabilidadeService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Usuario usuario = obterUsuarioLogado(request);

        if (usuario == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {
            action = "listar";
        }

        switch (action) {

            case "novo":
                abrirFormularioNovo(
                        request,
                        response
                );
                break;

            case "editar":
                abrirFormularioEditar(
                        request,
                        response,
                        usuario
                );
                break;

            case "listar":
            default:
                listar(
                        request,
                        response,
                        usuario
                );
                break;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Usuario usuario = obterUsuarioLogado(request);

        if (usuario == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        String action = request.getParameter("action");

        if (action == null || action.trim().isEmpty()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades"
            );

            return;
        }

        switch (action) {

            case "cadastrar":
                cadastrar(
                        request,
                        response,
                        usuario
                );
                break;

            case "atualizar":
                atualizar(
                        request,
                        response,
                        usuario
                );
                break;

            case "excluir":
                excluir(
                        request,
                        response,
                        usuario
                );
                break;

            default:
                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades"
                );
                break;
        }
    }

    /**
     * CREATE
     */
    private void cadastrar(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario
    ) throws IOException {

        String titulo =
                request.getParameter("titulo");

        String descricao =
                request.getParameter("descricao");

        String tipo =
                request.getParameter("tipo");

        Habilidade habilidade =
                new Habilidade(
                        titulo,
                        descricao,
                        tipo,
                        usuario.getId()
                );

        boolean sucesso =
                habilidadeService.cadastrar(habilidade);

        if (sucesso) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades?msg=criado"
            );

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades?msg=erro"
            );
        }
    }

    /**
     * READ
     */
    private void listar(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario
    ) throws ServletException, IOException {

        List<Habilidade> habilidades =
                habilidadeService.listarPorUsuario(
                        usuario.getId()
                );

        request.setAttribute(
                "habilidades",
                habilidades
        );

        String msg =
                request.getParameter("msg");

        if ("criado".equals(msg)) {

            request.setAttribute(
                    "msgSucesso",
                    "Conhecimento cadastrado com sucesso!"
            );

        } else if ("atualizado".equals(msg)) {

            request.setAttribute(
                    "msgSucesso",
                    "Conhecimento atualizado com sucesso!"
            );

        } else if ("excluido".equals(msg)) {

            request.setAttribute(
                    "msgSucesso",
                    "Conhecimento excluído com sucesso!"
            );

        } else if ("erro".equals(msg)) {

            request.setAttribute(
                    "msgErro",
                    "Não foi possível realizar a operação."
            );
        }

        request.getRequestDispatcher(
                "/habilidades.jsp"
        ).forward(
                request,
                response
        );
    }

    /**
     * Abre formulário de cadastro.
     */
    private void abrirFormularioNovo(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setAttribute(
                "modo",
                "novo"
        );

        request.getRequestDispatcher(
                "/habilidades.jsp"
        ).forward(
                request,
                response
        );
    }

    /**
     * Abre formulário de edição.
     */
    private void abrirFormularioEditar(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario
    ) throws ServletException, IOException {

        String idParametro =
                request.getParameter("id");

        if (idParametro == null) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades"
            );

            return;
        }

        try {

            Long id =
                    Long.parseLong(idParametro);

            Habilidade habilidade =
                    habilidadeService.buscarPorId(
                            id,
                            usuario.getId()
                    );

            if (habilidade == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades?msg=erro"
                );

                return;
            }

            request.setAttribute(
                    "habilidadeEditar",
                    habilidade
            );

            request.setAttribute(
                    "modo",
                    "editar"
            );

            request.getRequestDispatcher(
                    "/habilidades.jsp"
            ).forward(
                    request,
                    response
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades?msg=erro"
            );
        }
    }

    /**
     * UPDATE
     */
    private void atualizar(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario
    ) throws IOException {

        String idParametro =
                request.getParameter("id");

        String titulo =
                request.getParameter("titulo");

        String descricao =
                request.getParameter("descricao");

        String tipo =
                request.getParameter("tipo");

        try {

            Long id =
                    Long.parseLong(idParametro);

            Habilidade habilidade =
                    new Habilidade(
                            id,
                            titulo,
                            descricao,
                            tipo,
                            usuario.getId()
                    );

            boolean sucesso =
                    habilidadeService.atualizar(
                            habilidade
                    );

            if (sucesso) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades?msg=atualizado"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades?msg=erro"
                );
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades?msg=erro"
            );
        }
    }

    /**
     * DELETE
     */
    private void excluir(
            HttpServletRequest request,
            HttpServletResponse response,
            Usuario usuario
    ) throws IOException {

        String idParametro =
                request.getParameter("id");

        try {

            Long id =
                    Long.parseLong(idParametro);

            boolean sucesso =
                    habilidadeService.excluir(
                            id,
                            usuario.getId()
                    );

            if (sucesso) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades?msg=excluido"
                );

            } else {

                response.sendRedirect(
                        request.getContextPath()
                                + "/habilidades?msg=erro"
                );
            }

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/habilidades?msg=erro"
            );
        }
    }

    private Usuario obterUsuarioLogado(
            HttpServletRequest request
    ) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object objeto =
                session.getAttribute(
                        "usuarioLogado"
                );

        if (objeto instanceof Usuario) {
            return (Usuario) objeto;
        }

        return null;
    }
}