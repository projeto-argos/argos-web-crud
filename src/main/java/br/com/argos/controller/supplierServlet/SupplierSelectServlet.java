package br.com.argos.controller.supplierServlet;

import br.com.argos.service.SupplierService;
import br.com.argos.model.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SupplierSelectServlet", value = "/suppliers")
public class SupplierSelectServlet extends HttpServlet {

    // ATRIBUTO
    private SupplierService supplierService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        supplierService = new SupplierService();
    }

    // REQUISIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            // LISTAR
            List<Supplier> suppliers = supplierService.findAll();

            request.setAttribute("suppliers", suppliers);

            // ENCAMINHA PARA JSP
            request.getRequestDispatcher(
                    "/WEB-INF/views/suppliers/suppliers.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar fornecedores", e);
        }
    }
}