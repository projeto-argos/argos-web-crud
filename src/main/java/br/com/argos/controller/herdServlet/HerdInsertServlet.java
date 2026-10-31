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
import java.util.UUID;

@WebServlet(name = "HerdInsertServlet", value = "/herds/register")
public class HerdInsertServlet extends HttpServlet {

    // ATRIBUTOS
    private HerdService herdService;
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
        propertyService = new PropertyService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // BUSCA TODAS AS PROPRIEDADES
        List<Property> properties = propertyService.findAll();
        request.setAttribute("properties", properties);

        request.getRequestDispatcher(
                "/WEB-INF/views/herds/herds.jsp"
        ).forward(request, response);
    }

    // PROCESSA FORMULÁRIO
    @Override
    protected void doPost (
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
            String name =
                    request.getParameter("name");

            String breed =
                    request.getParameter("breed");

            String purpose =
                    request.getParameter("purpose");

            String headCountString =
                    request.getParameter("headCount");

            Integer headCount = null;

            if (headCountString != null &&
                    !headCountString.isBlank()) {
                headCount = Integer.valueOf(headCountString);
            }

            String propertyIdString =
                    request.getParameter("propertyId");

            UUID propertyId = null;

            if (propertyIdString != null &&
                    !propertyIdString.isBlank()) {
                propertyId = UUID.fromString(propertyIdString);
            }

            // CADASTRA REBANHO
            Herd newHerd = new Herd(
                    propertyId,
                    headCount,
                    purpose,
                    name,
                    breed
            );

            herdService.create(newHerd);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/herds");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            List<Herd> herds = herdService.findAll();
            List<Property> properties = propertyService.findAll();
            request.setAttribute("herds", herds);
            request.setAttribute("properties", properties);
            request.getRequestDispatcher("/WEB-INF/views/herds/herds.jsp").forward(request, response);
        }
    }
}