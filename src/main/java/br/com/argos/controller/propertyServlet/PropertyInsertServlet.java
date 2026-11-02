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
                for (String userIdString : userIdsString) {

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

        } catch (IllegalArgumentException e) {
            // DADOS EM FORMATO INVÁLIDO
            request.setAttribute("erro", "Dados inválidos, confira os campos.");
            List<Property> properties = propertyService.findAll();
            // BUSCA TODOS OS ENDERECOS UMA VEZ SO
            Map<UUID, Address> addressesById = new HashMap<>();
            for (Address address : addressService.findAll()) {
                addressesById.put(address.getIdAddress(), address);
            }

            Map<UUID, Address> propertyAddresses = new HashMap<>();

            for (Property property : properties) {
                Address address = addressesById.get(property.getAddressId());

                if (address != null) {
                    propertyAddresses.put(property.getIdProperty(), address);
                }
            }
            List<User> users = userService.findAll();
            request.setAttribute("properties", properties);
            request.setAttribute("propertyAddresses", propertyAddresses);
            request.setAttribute("users", users);
            request.setAttribute("allUsers", users);
            request.getRequestDispatcher("/WEB-INF/views/properties/properties.jsp").forward(request, response);
        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<Property> properties = propertyService.findAll();
            // BUSCA TODOS OS ENDERECOS UMA VEZ SO
            Map<UUID, Address> addressesById = new HashMap<>();
            for (Address address : addressService.findAll()) {
                addressesById.put(address.getIdAddress(), address);
            }

            Map<UUID, Address> propertyAddresses = new HashMap<>();

            for (Property property : properties) {
                Address address = addressesById.get(property.getAddressId());

                if (address != null) {
                    propertyAddresses.put(property.getIdProperty(), address);
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