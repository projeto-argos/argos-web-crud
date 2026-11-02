package br.com.argos.controller.herdServlet;

import br.com.argos.exceptions.ArgosException;
import br.com.argos.model.Herd;
import br.com.argos.model.Property;
import br.com.argos.service.HerdService;
import br.com.argos.service.PropertyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "HerdDeleteServlet", value = "/herds/delete")
public class HerdDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private HerdService herdService;
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
        propertyService = new PropertyService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String propertyIdParam = request.getParameter("propertyId");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // EXCLUI REBANHO
                UUID id = UUID.fromString(idParam);
                herdService.delete(id);

                response.sendRedirect(request.getContextPath() + "/herds");
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir rebanho", e);
            }
        }

        if (propertyIdParam != null && !propertyIdParam.isBlank()) {
            try {
                // EXCLUI OS REBANHOS DA PROPRIEDADE
                herdService.deleteByProperty(UUID.fromString(propertyIdParam));

            } catch (ArgosException e) {
                // TRATA ERRO
                request.setAttribute("erro", e.getMessage());
                List<Herd> herds = herdService.findAll();
                List<Property> properties = propertyService.findAll();
                request.setAttribute("herds", herds);
                request.setAttribute("properties", properties);
                request.getRequestDispatcher("/WEB-INF/views/herds/herds.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir rebanho", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/herds");
    }
}