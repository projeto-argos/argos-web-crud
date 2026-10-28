package br.com.argos.controller.propertyServlet;

import br.com.argos.model.Address;
import br.com.argos.model.Property;
import br.com.argos.model.User;
import br.com.argos.service.AddressService;
import br.com.argos.service.PropertyService;
import br.com.argos.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "PropertyUpdateServlet", value = "/properties/edit")
public class PropertyUpdateServlet extends HttpServlet {

    // ATRIBUTOS
    private PropertyService propertyService;
    private AddressService addressService;
    private UserService userService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
        addressService = new AddressService();
        userService = new UserService();
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
                UUID idProperty = UUID.fromString(idParam);
                Property property = propertyService.findById(idProperty);

                if (property != null) {
                    Address address = addressService.findById(property.getAddressId());

                    List<User> allUsers = userService.findAll();
                    List<User> users = new ArrayList<>();

                    for (UUID userId : property.getUserIds()) {
                        User user = userService.findById(userId);

                        if (user != null) {
                            users.add(user);
                        }
                    }
                    request.setAttribute("property", property);
                    request.setAttribute("address", address);
                    request.setAttribute("users", users);
                    request.setAttribute("allUsers", allUsers);
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
            // RECEBE DADOS DA PROPRIEDADE
            UUID idProperty =
                    UUID.fromString(request.getParameter("idProperty"));

            String name =
                    request.getParameter("name");

            String phone =
                    request.getParameter("phone");

            String cnpj =
                    request.getParameter("cnpj");

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
            // RECEBE OS DADOS DO ENDEREÇO
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

            Property property =
                    propertyService.findById(idProperty);

            UUID addressId =
                    property.getAddressId();

            // ATUALIZA O ENDEREÇO
            Address addressToUpdate = new Address(
                    addressId,
                    number,
                    complement,
                    street,
                    city,
                    state,
                    zipCode
            );

            addressService.update(addressToUpdate);

            // ATUALIZA A PROPRIEDADE
            Property propertyToUpdate = new Property(
                    idProperty,
                    addressId,
                    userIds,
                    name,
                    phone,
                    cnpj
            );

            propertyService.update(propertyToUpdate);

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