package br.com.argos.controller.adminServlet;

import br.com.argos.service.AdminService;
import br.com.argos.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginAdminServlet", value = "/login")
public class LoginAdminServlet extends HttpServlet {

    // ATRIBUTO
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
    }

    // EXIBE A TELA DE LOGIN
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/login.jsp"
        ).forward(request, response);
    }

    // PROCESSA O LOGIN
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

            if (
                    email == null ||
                            email.isBlank() ||
                            password == null ||
                            password.isBlank()
            ) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                request.setAttribute("error", "Email e senha são obrigatórios");
                request.getRequestDispatcher(
                        "/WEB-INF/views/login.jsp"
                ).forward(request, response);
                return;
            }

            // AUTENTICA ADMINISTRADOR USANDO O SERVICE
            Admin admin =
                    adminService.authenticate(email, password);

            if (admin != null) {
                // CRIA SESSÃO
                HttpSession session =
                        request.getSession();

                session.setAttribute("adminLoggedIn", admin);
                session.setAttribute("adminId", admin.getIdAdmin());
                session.setAttribute("adminFullName", admin.getFullName());
                session.setAttribute("adminEmail", admin.getEmail());

                // REDIRECIONA PARA HOME
                response.sendRedirect(
                        request.getContextPath() + "/home"
                );
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                request.setAttribute("error", "Email ou senha inválidos");
                request.getRequestDispatcher(
                        "/WEB-INF/views/login.jsp"
                ).forward(request, response);
            }

        } catch (Exception e) {
            // TRATA ERRO
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.setAttribute("error", "Email ou senha inválidos");
            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp"
            ).forward(request, response);
        }
    }
}