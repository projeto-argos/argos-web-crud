package br.com.argos.controller.authServlet;

import br.com.argos.model.User;
import br.com.argos.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    // ATRIBUTO
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // EXIBE O FORMULÁRIO DE CADASTRO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/register.jsp"
        ).forward(request, response);
    }

    // PROCESSA O CADASTRO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE OS DADOS
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

            String birthDateParameter =
                    request.getParameter("birthDate");

            LocalDate birthDate = null;

            if (birthDateParameter != null &&
                    !birthDateParameter.isBlank()) {
                birthDate = LocalDate.parse(birthDateParameter);
            }

            // CADASTRA USUÁRIO
            User newUser = new User(
                    fullName,
                    cpf,
                    email,
                    phone,
                    role,
                    password,
                    birthDate
            );

            userService.create(newUser);

            // REDIRECIONA PARA O LOGIN
            response.sendRedirect(
                    request.getContextPath() + "/login");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }
}
