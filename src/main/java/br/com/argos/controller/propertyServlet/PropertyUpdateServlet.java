
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

        String idParam =
                request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {
            response.sendRedirect(
                    request.getContextPath() + "/properties"
            );
            return;
        }

        try {
            UUID idProperty = UUID.fromString(idParam);

            Property property =
                    propertyService.findById(idProperty);

            if (property == null) {
                response.sendRedirect(
                        request.getContextPath() + "/properties"
                );
                return;
            }

            // BUSCA ENDEREÇO DA PROPRIEDADE
            Address address =
                    addressService.findById(property.getAddressId());

            // BUSCA USUÁRIOS VINCULADOS
            List<User> selectedUsers = new ArrayList<>();

            for (UUID userId : property.getUserIds()) {
                User user = userService.findById(userId);

                if (user != null) {
                    selectedUsers.add(user);
                }
            }

            // ENVIA DADOS PARA A JSP
            request.setAttribute("property", property);
            request.setAttribute("address", address);
            request.setAttribute("selectedUsers", selectedUsers);

            request.getRequestDispatcher(
                    "/WEB-INF/views/properties/properties.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            throw new ServletException(
                    "Erro ao buscar propriedade para edição",
                    e
            );
        }
    }

    // PROCESSA A ATUALIZAÇÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {

            // RECEBE ID DA PROPRIEDADE
            UUID idProperty =
                    UUID.fromString(request.getParameter("idProperty"));

            // BUSCA REGISTRO ATUAL
            Property existingProperty =
                    propertyService.findById(idProperty);

            if (existingProperty == null) {
                throw new IllegalArgumentException(
                        "Propriedade não encontrada."
                );
            }

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

                    if (userIdString != null
                            && !userIdString.isBlank()) {

                        UUID userId =
                                UUID.fromString(userIdString.trim());

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

            if (numberString != null
                    && !numberString.isBlank()) {

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


            // BUSCA ENDEREÇO ATUAL
            UUID addressId = existingProperty.getAddressId();

            Address existingAddress =
                    addressService.findById(addressId);

            if (existingAddress == null) {
                throw new IllegalArgumentException(
                        "Endereço da propriedade não encontrado."
                );
            }


            // ATUALIZA ENDEREÇO EXISTENTE
            Address addressToUpdate = new Address(
                    existingAddress.getIdAddress(),
                    number,
                    complement,
                    street,
                    city,
                    state,
                    zipCode,
                    null,
                    existingAddress.isActive()
            );

            addressService.update(addressToUpdate);


            // ATUALIZA PROPRIEDADE
            Property propertyToUpdate = new Property(
                    idProperty,
                    addressId,
                    userIds,
                    name,
                    phone,
                    cnpj,
                    null,
                    existingProperty.isActive()
            );

            propertyService.update(propertyToUpdate);


            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/properties"
            );

        } catch (Exception e) {

            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());

            request.getRequestDispatcher(
                    "/WEB-INF/views/properties/properties.jsp"
            ).forward(request, response);
        }
    }
}
