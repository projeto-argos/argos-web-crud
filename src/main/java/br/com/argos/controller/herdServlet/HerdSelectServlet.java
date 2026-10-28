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
import java.util.List;

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
            // LISTA REBANHOS E PROPRIEDADES
            List<Herd> herds = herdService.findAll();
            List<Property> properties = propertyService.findAll();
            request.setAttribute("herds", herds);
            request.setAttribute("properties", properties);

            request.getRequestDispatcher(
                    "/WEB-INF/views/herds/herds.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // TRATA ERRO
            throw new ServletException("Erro ao listar rebanhos", e);
        }
    }
}