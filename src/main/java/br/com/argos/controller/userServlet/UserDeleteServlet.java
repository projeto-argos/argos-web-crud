package br.com.argos.controller.userServlet;

import br.com.argos.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "UserDeleteServlet", value = "/users/excluir")
public class UserDeleteServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        userService = new UserService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isBlank()) {
            try {
                UUID id = UUID.fromString(idParam);
                userService.delete(id);
            } catch (SQLException e) {
                throw new ServletException("Erro ao excluir usuário", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/users");
    }
}