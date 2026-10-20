package br.com.argos.controller.supplierServlet;

import br.com.argos.service.SupplierService;
import br.com.argos.model.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SupplierUpdateServlet", value = "/suppliers/edit")
public class SupplierUpdateServlet extends HttpServlet {

    // ATRIBUTO
    private SupplierService supplierService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        supplierService = new SupplierService();
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
                Supplier supplier = supplierService.findById(id);

                if (supplier != null) {
                    request.setAttribute("supplier", supplier);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/suppliers/form.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar fornecedor para edição", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/suppliers");
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
            UUID idSupplier =
                    UUID.fromString(request.getParameter("idSupplier"));

            String fullName =
                    request.getParameter("fullName");

            String cnpj =
                    request.getParameter("cnpj");

            String phone =
                    request.getParameter("phone");

            String email =
                    request.getParameter("email");

            String addressIdParam =
                    request.getParameter("addressId");

            UUID addressId = null;

            if (
                    addressIdParam != null &&
                            !addressIdParam.isBlank()
            ) {
                addressId =
                        UUID.fromString(addressIdParam);
            }

            boolean active =
                    request.getParameter("active") != null &&
                            request.getParameter("active").equals("true");

            // ATUALIZA FORNECEDOR
            Supplier supplierToUpdate = new Supplier(
                    idSupplier,
                    addressId,
                    fullName,
                    cnpj,
                    phone,
                    email,
                    null,
                    active
            );

            supplierService.update(supplierToUpdate);

            // REDIRECIONA
            response.sendRedirect(request.getContextPath() + "/suppliers");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/suppliers/form.jsp").forward(request, response);
        }
    }
}