
package br.com.argos.controller.authServlet;

import br.com.argos.exceptions.ValidationException;
import br.com.argos.model.Admin;
import br.com.argos.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    // ATRIBUTOS
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
    }

    // EXIBE PAGINA DE LOGIN
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    // PROCESSA LOGIN
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        try {
            // RECEBE DADOS
            String email =
                    request.getParameter("email");

            String password =
                    request.getParameter("password");

            // VEREFICA SE EMAIL E SENHA FORAM PASSADOS
            if (email == null ||
                    email.isBlank() ||
                    password == null ||
                    password.isBlank()) {
                request.setAttribute("erro", "Informe o e-mail e a senha.");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp"
                        ).forward(request, response);
                return;
            }

            Admin admin = adminService.authenticate(email, password);

            // ENCERRA E CRIA SESSAO
            HttpSession previousSession = request.getSession(false);
            if (previousSession != null) {
                previousSession.invalidate();
            }
            HttpSession session = request.getSession(true);

            // Armazena os dados do administrador autenticado.
            Map<String, String> loggedUser = new HashMap<>();
            loggedUser.put("id", admin.getIdAdmin().toString());
            loggedUser.put("name", admin.getFullName());
            loggedUser.put("type", "ADMIN");
            session.setAttribute("usuarioLogado", loggedUser);

            response.sendRedirect(
                    request.getContextPath() + "/home");

        } catch (ValidationException e) {
            request.setAttribute("erro", "E-mail ou senha inválidos.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
        }
    }
}
