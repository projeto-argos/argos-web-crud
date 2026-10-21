package br.com.argos.controller.propertyServlet;

import br.com.argos.service.PropertyService;
import br.com.argos.model.Property;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PropertyUpdateServlet", value = "/properties/edit")
public class PropertyUpdateServlet extends HttpServlet {

    // ATRIBUTO
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
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
                Property property = propertyService.findById(id);

                if (property != null) {
                    request.setAttribute("property", property);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/properties/properties.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar propriedade para edição", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/properties");
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
            UUID idProperty =
                    UUID.fromString(request.getParameter("idProperty"));

            String name =
                    request.getParameter("name");

            String cnpj =
                    request.getParameter("cnpj");

            String phone =
                    request.getParameter("phone");

            String userIdParam =
                    request.getParameter("userId");

            UUID userId = null;

            if (
                    userIdParam != null &&
                            !userIdParam.isBlank()
            ) {
                userId =
                        UUID.fromString(userIdParam);
            }

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

            // ATUALIZA PROPRIEDADE
            Property propertyToUpdate = new Property(
                    idProperty,
                    userId,
                    addressId,
                    name,
                    phone,
                    cnpj,
                    null,
                    active
            );

            propertyService.update(propertyToUpdate);

            // REDIRECIONA
            response.sendRedirect(request.getContextPath() + "/properties");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/properties/properties.jsp").forward(request, response);
        }
    }
}