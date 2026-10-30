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
import java.util.UUID;

@WebServlet(name = "AdminUpdateServlet", value = "/admins/edit")
public class AdminUpdateServlet extends HttpServlet {

    // ATRIBUTOS
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
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
                Admin admin = adminService.findById(id);

                if (admin != null) {
                    request.setAttribute("admin", admin);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/admins/admins.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar administrador para edição", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admins");
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
            UUID idAdmin =
                    UUID.fromString(request.getParameter("idAdmin"));

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

            // ATUALIZA ADMINISTRADOR
            Admin adminToUpdate = new Admin(
                    idAdmin,
                    fullName,
                    cpf,
                    email,
                    phone
            );

            adminService.update(adminToUpdate);

            // SENHA NOVA PREENCHIDA
            if (password != null &&
                    !password.isBlank()) {
                try {
                    adminService.changePassword(idAdmin, password);

                } catch (Exception e) {
                    // DADOS SALVOS, MAS A SENHA NÃO
                    request.setAttribute("erro",
                            "Dados salvos, mas a senha não foi alterada: " + e.getMessage());
                    List<Admin> admins = adminService.findAll();
                    request.setAttribute("admins", admins);
                    request.getRequestDispatcher("/WEB-INF/views/admins/admins.jsp").forward(request, response);
                    return;
                }
            }

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