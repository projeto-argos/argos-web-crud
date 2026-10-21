package br.com.argos.controller.supplierServlet;

import br.com.argos.service.SupplierService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SupplierDeleteServlet", value = "/suppliers/delete")
public class SupplierDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private SupplierService supplierService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        supplierService = new SupplierService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {

                // EXCLUI FORNECEDOR
                UUID id = UUID.fromString(idParam);
                supplierService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir fornecedor", e);
            }
        }

        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/suppliers");
    }
}