package br.com.argos.controller.adminServlet;

import br.com.argos.exceptions.ArgosException;
import br.com.argos.model.Admin;
import br.com.argos.service.AdminService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AdminDeleteServlet", value = "/admins/delete")
public class AdminDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String cpfParam = request.getParameter("cpf");

        if (idParam != null && !idParam.isBlank()) {
            try {
                UUID id = UUID.fromString(idParam);

                // NAO DEIXA EXCLUIR O ADMIN LOGADO
                UUID loggedAdminId = (UUID) request.getSession().getAttribute("loggedAdminId");
                if (id.equals(loggedAdminId)) {
                    request.setAttribute("erro", "Você não pode excluir o próprio administrador.");
                    List<Admin> admins = adminService.findAll();
                    request.setAttribute("admins", admins);
                    request.getRequestDispatcher("/WEB-INF/views/admins/admins.jsp").forward(request, response);
                    return;
                }

                // EXCLUI ADMINISTRADOR
                adminService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir administrador", e);
            }
        }

        if (cpfParam != null && !cpfParam.isBlank()) {
            try {
                // EXCLUI ADMINISTRADOR PELO CPF
                UUID loggedAdminId = (UUID) request.getSession().getAttribute("loggedAdminId");
                adminService.deleteByCpf(cpfParam, loggedAdminId);

            } catch (ArgosException e) {
                // TRATA ERRO
                request.setAttribute("erro", e.getMessage());
                List<Admin> admins = adminService.findAll();
                request.setAttribute("admins", admins);
                request.getRequestDispatcher("/WEB-INF/views/admins/admins.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir administrador", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/admins");
    }
}