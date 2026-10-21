package br.com.argos.controller.userServlet;

import br.com.argos.service.UserService;
import br.com.argos.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserSelectServlet", value = "/users")
public class UserSelectServlet extends HttpServlet {

    // ATRIBUTO
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // REQUISIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException{

        try {
            // LISTAR
            List<User> users = userService.findAll();

            request.setAttribute("users", users);

            // ENCAMINHA PARA JSP
            request.getRequestDispatcher(
                    "/WEB-INF/views/users/list.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar usuários", e);
        }
    }
}