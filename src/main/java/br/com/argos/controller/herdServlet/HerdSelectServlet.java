package br.com.argos.controller.herdServlet;

import br.com.argos.service.HerdService;
import br.com.argos.service.PropertyService;
import br.com.argos.model.Herd;
import br.com.argos.model.Property;
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

@WebServlet(name = "HerdSelectServlet", value = "/herds")
public class HerdSelectServlet extends HttpServlet {

    // ATRIBUTOS
    private HerdService herdService;
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
        propertyService = new PropertyService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // RECEBE FILTROS
            String name =
                    request.getParameter("name");

            String breed =
                    request.getParameter("breed");

            String purpose =
                    request.getParameter("purpose");

            String propertyId =
                    request.getParameter("propertyId");

            // GUARDA OS FILTROS PARA A TELA
            Map<String, String> filter = new HashMap<>();
            filter.put("name", name);
            filter.put("breed", breed);
            filter.put("purpose", purpose);
            filter.put("propertyId", propertyId);
            request.setAttribute("filter", filter);

            // LISTA REBANHOS E PROPRIEDADES
            List<Herd> herds = herdService.filter(null, purpose, name, breed);
            List<Property> properties = propertyService.findAll();

            // FILTRA POR PROPRIEDADE
            if (propertyId != null && !propertyId.isBlank()) {
                List<Herd> herdsByProperty = new ArrayList<>();

                for (int i = 0; i < herds.size(); i++) {
                    Herd herd = herds.get(i);

                    if (propertyId.equals(String.valueOf(herd.getPropertyId()))) {
                        herdsByProperty.add(herd);
                    }
                }
                herds = herdsByProperty;
            }
            request.setAttribute("herds", herds);
            request.setAttribute("properties", properties);

            request.getRequestDispatcher(
                    "/WEB-INF/views/herds/herds.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar rebanhos", e);
        }
    }
}