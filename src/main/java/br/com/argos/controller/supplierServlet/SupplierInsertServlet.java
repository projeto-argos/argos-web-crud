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

@WebServlet(name = "SupplierInsertServlet", value = "/suppliers/register")
public class SupplierInsertServlet extends HttpServlet {

    // ATRIBUTO
    private SupplierService supplierService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        supplierService = new SupplierService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/suppliers/form.jsp"
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

            String cnpj =
                    request.getParameter("cnpj");

            String phone =
                    request.getParameter("phone");

            String email =
                    request.getParameter("email");

            String addressIdString =
                    request.getParameter("addressId");

            UUID addressId = null;

            if (
                    addressIdString!= null &&
                            !addressIdString.isBlank()
            ) {
                addressId =
                        UUID.fromString(addressIdString);
            }

            boolean active =
                    request.getParameter("active") != null &&
                            request.getParameter("active").equals("true");

            // CADASTRA FORNECEDOR
            Supplier newSupplier = new Supplier(
                    addressId,
                    fullName,
                    cnpj,
                    phone,
                    email,
                    active
            );

            supplierService.create(newSupplier);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/suppliers");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/suppliers/form.jsp").forward(request, response);
        }
    }
}