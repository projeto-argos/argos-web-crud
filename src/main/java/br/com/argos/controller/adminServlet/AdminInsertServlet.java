package br.com.argos.controller.adminServlet;

import br.com.argos.service.AdminService;
import br.com.argos.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminInsertServlet", value = "/admins/register")
public class AdminInsertServlet extends HttpServlet {

    // ATRIBUTOS
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/admins/admins.jsp"
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

            String password =
                    request.getParameter("password");

            // CADASTRA ADMINISTRADOR
            Admin newAdmin = new Admin(
                    fullName,
                    cpf,
                    email,
                    phone,
                    password
            );

            adminService.create(newAdmin);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/admins");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<Admin> admins = adminService.findAll();
            request.setAttribute("admins", admins);
            request.getRequestDispatcher("/WEB-INF/views/admins/admins.jsp").forward(request, response);
        }
    }
}