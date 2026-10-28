package br.com.argos.controller.propertyServlet;

import br.com.argos.model.Property;
import br.com.argos.service.AddressService;
import br.com.argos.service.PropertyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PropertyDeleteServlet", value = "/properties/delete")
public class PropertyDeleteServlet extends HttpServlet {

    // ATRIBUTOS
    private PropertyService propertyService;
    private AddressService addressService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
        addressService = new AddressService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // RECEBE O ID DA PROPRIEDADE
        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // BUSCA A PROPRIEDADE
                UUID idProperty = UUID.fromString(idParam);
                Property property = propertyService.findById(idProperty);

                if (property != null) {
                    // OBTÉM O ID DO ENDEREÇO
                    UUID addressId = property.getAddressId();

                    // EXCLUI A PROPRIEDADE
                    propertyService.delete(idProperty);

                    // EXCLUI O ENDEREÇO ASSOCIADO
                    if (addressId != null) {
                        addressService.delete(addressId);
                    }
                }
            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir propriedade e endereço", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/properties");
    }
}