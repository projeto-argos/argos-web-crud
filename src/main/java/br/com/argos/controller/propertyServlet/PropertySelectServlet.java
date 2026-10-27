package br.com.argos.controller.propertyServlet;

import br.com.argos.service.PropertyService;
import br.com.argos.model.Property;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PropertySelectServlet", value = "/properties")
public class PropertySelectServlet extends HttpServlet {

    // ATRIBUTO
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // LISTAR
            List<Property> properties = propertyService.findAll();
            request.setAttribute("properties", properties);

            request.getRequestDispatcher(
                    "/WEB-INF/views/properties/properties.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar propriedades", e);
        }
    }
}