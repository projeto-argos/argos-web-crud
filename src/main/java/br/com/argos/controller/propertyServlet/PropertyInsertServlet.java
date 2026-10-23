package br.com.argos.controller.propertyServlet;

import br.com.argos.model.Address;
import br.com.argos.model.Property;
import br.com.argos.service.AddressService;
import br.com.argos.service.PropertyService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "PropertyInsertServlet", value = "/properties/register")
public class PropertyInsertServlet extends HttpServlet {

    // ATRIBUTOS
    private PropertyService propertyService;
    private AddressService addressService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
        addressService = new AddressService();
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

            // RECEBE DADOS DA PROPRIEDADE

            String name =
                    request.getParameter("name");

            String phone =
                    request.getParameter("phone");

            String cnpj =
                    request.getParameter("cnpj");


            // RECEBE IDS DOS USUÁRIOS SELECIONADOS

            String[] userIdsString =
                    request.getParameterValues("userIds");

            List<UUID> userIds = new ArrayList<>();

            if (userIdsString != null) {
                for (int i = 0; i < userIdsString.length; i++) {
                    String userIdString = userIdsString[i];

                    if (userIdString != null && !userIdString.isBlank()) {

                        UUID userId = UUID.fromString(userIdString.trim());

                        if (!userIds.contains(userId)) {
                            userIds.add(userId);
                        }
                    }
                }
            }


            // RECEBE DADOS DO ENDEREÇO
            String numberString =
                    request.getParameter("number");

            Integer number = null;

            if (numberString != null &&
                            !numberString.isBlank()) {
                number = Integer.valueOf(numberString);
            }

            String complement =
                    request.getParameter("complement");

            String street =
                    request.getParameter("street");

            String city =
                    request.getParameter("city");

            String state =
                    request.getParameter("state");

            String zipCode =
                    request.getParameter("zipCode");

            // CADASTRA ENDEREÇO
            Address newAddress = new Address(
                    number,
                    complement,
                    street,
                    city,
                    state,
                    zipCode
            );

            UUID addressId =
                    addressService.createAndReturnId(newAddress);


            // CADASTRA PROPRIEDADE
            Property newProperty = new Property(
                    addressId,
                    userIds,
                    name,
                    phone,
                    cnpj
            );

            propertyService.create(newProperty);

            // REDIRECIONA
            response.sendRedirect(request.getContextPath() + "/properties");

        } catch (Exception e) {

            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());

            request.getRequestDispatcher("/WEB-INF/views/properties/properties.jsp").forward(request, response);
        }
    }
}