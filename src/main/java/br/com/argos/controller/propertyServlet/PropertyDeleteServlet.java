package br.com.argos.controller.propertyServlet;

import br.com.argos.exceptions.ArgosException;
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

@WebServlet(name = "PropertyDeleteServlet", value = "/properties/delete")
public class PropertyDeleteServlet extends HttpServlet {

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

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String addressIdParam = request.getParameter("addressId");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // BUSCA A PROPRIEDADE
                UUID idProperty = UUID.fromString(idParam);
                Property property = propertyService.findById(idProperty);

                if (property != null) {
                    UUID addressId = property.getAddressId();

                    // EXCLUI A PROPRIEDADE
                    propertyService.delete(idProperty);

                    // EXCLUI O ENDEREÇO
                    if (addressId != null) {
                        addressService.delete(addressId);
                    }
                }

                response.sendRedirect(request.getContextPath() + "/properties");
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir propriedade e endereço", e);
            }
        }

        if (addressIdParam != null && !addressIdParam.isBlank()) {
            try {
                // EXCLUI A PROPRIEDADE PELO ENDEREÇO
                UUID addressId = UUID.fromString(addressIdParam);
                propertyService.deleteByAddress(addressId);

                // EXCLUI O ENDEREÇO
                addressService.delete(addressId);

            } catch (ArgosException e) {
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
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir propriedade e endereço", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/properties");
    }
}