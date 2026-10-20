package br.com.argos.controller.userServlet;

import br.com.argos.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "UserDeleteServlet", value = "/users/delete")
public class UserDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {

                // EXCLUI USUÁRIO
                UUID id = UUID.fromString(idParam);
                userService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir usuário", e);
            }
        }

        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/users");
    }
}