package br.com.argos.controller.userServlet;

import br.com.argos.service.UserService;
import br.com.argos.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "UserInsertServlet", value = "/users/register")
public class UserInsertServlet extends HttpServlet {

    // ATRIBUTO
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/users/users.jsp"
                ).forward(request, response);
    }


    // PROCESSA FORMULÁRIO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
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

            String password =
                    request.getParameter("password");

            String birthDateString =
                    request.getParameter("birthDate");


            LocalDate birthDate = null;

            if (
                    birthDateString != null &&
                            !birthDateString.isBlank()
            ) {
                birthDate = LocalDate.parse(birthDateString);
            }

            boolean active =
                    request.getParameter("active") != null &&
                    request.getParameter("active").equals("true");

            // CADASTRA USUÁRIO

            User newUser = new User(
                    fullName,
                    cpf, email,
                    phone,
                    role,
                    password,
                    birthDate,
                    active
            );

            userService.create(newUser);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/users");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/users/users.jsp").forward(request, response);
        }
    }
}