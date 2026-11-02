package br.com.argos.controller.userServlet;

import br.com.argos.exceptions.ArgosException;
import br.com.argos.model.User;
import br.com.argos.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserReactivateServlet", value = "/users/reactivate")
public class UserReactivateServlet extends HttpServlet {

    // ATRIBUTO
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // PROCESSA A REATIVAÇÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String cpfParam = request.getParameter("cpf");

        try {
            // REATIVA USUÁRIO PELO CPF
            userService.reactivateByCpf(cpfParam);

            // REDIRECIONA
            response.sendRedirect(request.getContextPath() + "/users");

        } catch (ArgosException e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<User> users = userService.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/users/users.jsp").forward(request, response);

        } catch (Exception e) {
            // TRATA ERRO
            throw new ServletException("Erro ao reativar usuário", e);
        }
    }
}
