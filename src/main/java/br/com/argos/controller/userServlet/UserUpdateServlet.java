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
import java.util.List;
import java.util.UUID;

@WebServlet(name = "UserUpdateServlet", value = "/users/edit")
public class UserUpdateServlet extends HttpServlet {

    // ATRIBUTOS
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        userService = new UserService();
    }

    // EXIBE FORMULÁRIO DE EDIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                UUID id = UUID.fromString(idParam);
                User user = userService.findById(id);

                if (user != null) {
                    request.setAttribute("user", user);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/users/users.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar usuário para edição", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/users");
    }

    // PROCESSA A ATUALIZAÇÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
            UUID idUser =
                    UUID.fromString(request.getParameter("idUser"));

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

            if (birthDateString != null &&
                            !birthDateString.isBlank()) {
                birthDate =
                        LocalDate.parse(birthDateString);
            }

            // ATUALIZA USUÁRIO
            User userToUpdate = new User(
                    idUser,
                    fullName,
                    cpf,
                    email,
                    phone,
                    role,
                    birthDate
            );

            userService.update(userToUpdate);

            // SENHA NOVA PREENCHIDA
            if (password != null &&
                    !password.isBlank()) {
                try {
                    userService.changePassword(idUser, password);

                } catch (Exception e) {
                    // DADOS SALVOS, MAS A SENHA NÃO
                    request.setAttribute("erro",
                            "Dados salvos, mas a senha não foi alterada: " + e.getMessage());
                    List<User> users = userService.findAll();
                    request.setAttribute("users", users);
                    request.getRequestDispatcher("/WEB-INF/views/users/users.jsp").forward(request, response);
                    return;
                }
            }

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/users");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<User> users = userService.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/users/users.jsp").forward(request, response);
        }
    }
}