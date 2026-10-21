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

@WebServlet(name = "PropertyInsertServlet", value = "/properties/register")
public class PropertyInsertServlet extends HttpServlet {

    // ATRIBUTO
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
    }

    // EXIBE FORMULÁRIO DE CADASTRO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/properties/properties.jsp"
        ).forward(request, response);
    }

    // PROCESSA FORMULÁRIO DE CADASTRO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
            String name =
                    request.getParameter("name");

            String cnpj =
                    request.getParameter("cnpj");

            String phone =
                    request.getParameter("phone");

            String userIdString =
                    request.getParameter("userId");

            UUID userId = null;

            if (
                    userIdString != null &&
                            !userIdString.isBlank()
            ) {
                userId = UUID.fromString(userIdString);
            }

            String addressIdString =
                    request.getParameter("addressId");

            UUID addressId = null;

            if (
                    addressIdString != null &&
                            !addressIdString.isBlank()
            ) {
                addressId = UUID.fromString(addressIdString);
            }

            boolean active =
                    request.getParameter("active") != null &&
                            request.getParameter("active").equals("true");

            // CADASTRA PROPRIEDADE
            Property newProperty = new Property(
                    userId,
                    addressId,
                    name,
                    phone,
                    cnpj,
                    active
            );

            propertyService.create(newProperty);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/properties");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/properties/properties.jsp").forward(request, response);
        }
    }
}