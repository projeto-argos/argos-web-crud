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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "PropertySelectServlet", value = "/properties")
public class PropertySelectServlet extends HttpServlet {

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

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // LISTAR PROPRIEDADES
            List<Property> properties = propertyService.findAll();
            Map<UUID, Address> propertyAddresses = new HashMap<>();

            for (int i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                UUID addressId = property.getAddressId();

                if (addressId != null) {
                    Address address = addressService.findById(addressId);

                    if (address != null) {
                        propertyAddresses.put(
                                property.getIdProperty(),
                                address
                        );
                    }
                }
            }

            // LISTAR USUÁRIOS
            List<User> users = userService.findAll();
            request.setAttribute("properties", properties);
            request.setAttribute("propertyAddresses", propertyAddresses);
            request.setAttribute("users", users);
            request.setAttribute("allUsers", users);

            request.getRequestDispatcher(
                    "/WEB-INF/views/properties/properties.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar propriedades", e);
        }
    }
}
