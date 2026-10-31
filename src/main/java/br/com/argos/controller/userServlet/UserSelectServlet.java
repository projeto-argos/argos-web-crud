package br.com.argos.controller.userServlet;

import br.com.argos.service.UserService;
import br.com.argos.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserSelectServlet", value = "/users")
public class UserSelectServlet extends HttpServlet {

    // ATRIBUTOS
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException{
        try {
            // RECEBE FILTROS
            String fullName =
                    request.getParameter("fullName");

            String cpf =
                    request.getParameter("cpf");

            String email =
                    request.getParameter("email");

            String phone =
                    request.getParameter("phone");

            String role =
                    request.getParameter("role");

            // GUARDA OS FILTROS PARA A TELA
            Map<String, String> filter = new HashMap<>();
            filter.put("fullName", fullName);
            filter.put("cpf", cpf);
            filter.put("email", email);
            filter.put("phone", phone);
            filter.put("role", role);
            request.setAttribute("filter", filter);

            // LISTAR
            List<User> users = userService.filter(fullName, cpf, email, phone, role);
            request.setAttribute("users", users);

            request.getRequestDispatcher(
                    "/WEB-INF/views/users/users.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar usuários", e);
        }
    }
}