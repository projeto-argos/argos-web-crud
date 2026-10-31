package br.com.argos.controller.adminServlet;

import br.com.argos.service.AdminService;
import br.com.argos.model.Admin;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminSelectServlet", value = "/admins")
public class AdminSelectServlet extends HttpServlet {

    // ATRIBUTO
    private AdminService adminService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        adminService = new AdminService();
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

            // GUARDA OS FILTROS PARA A TELA
            Map<String, String> filter = new HashMap<>();
            filter.put("fullName", fullName);
            filter.put("cpf", cpf);
            filter.put("email", email);
            filter.put("phone", phone);
            request.setAttribute("filter", filter);

            // LISTAR
            List<Admin> admins = adminService.filter(fullName, cpf, email, phone, null);
            request.setAttribute("admins", admins);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admins/admins.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar administradores", e);
        }
    }
}