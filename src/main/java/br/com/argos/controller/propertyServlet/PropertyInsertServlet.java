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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "PropertyInsertServlet", value = "/properties/register")
public class PropertyInsertServlet extends HttpServlet {

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

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // BUSCA TODOS OS USUÁRIOS
        List<User> allUsers = userService.findAll();
        request.setAttribute("allUsers", allUsers);

        request.getRequestDispatcher(
                "/WEB-INF/views/properties/properties.jsp"
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
            // RECEBE DADOS DA PROPRIEDADE
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

            try {
                propertyService.create(newProperty);

            } catch (Exception e) {
                // DESFAZ O ENDEREÇO SE A PROPRIEDADE FALHAR
                addressService.delete(addressId);
                throw e;
            }

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/properties");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<Property> properties = propertyService.findAll();
            Map<UUID, Address> propertyAddresses = new HashMap<>();

            for (int i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                UUID addressId = property.getAddressId();

                if (addressId != null) {
                    Address address = addressService.findById(addressId);

                    if (address != null) {
                        propertyAddresses.put(property.getIdProperty(), address);
                    }
                }
            }
            List<User> users = userService.findAll();
            request.setAttribute("properties", properties);
            request.setAttribute("propertyAddresses", propertyAddresses);
            request.setAttribute("users", users);
            request.setAttribute("allUsers", users);
            request.getRequestDispatcher("/WEB-INF/views/properties/properties.jsp").forward(request, response);
        }
    }
}