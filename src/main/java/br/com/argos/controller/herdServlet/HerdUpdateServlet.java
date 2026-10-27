package br.com.argos.controller.herdServlet;

import br.com.argos.service.HerdService;
import br.com.argos.model.Herd;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "HerdUpdateServlet", value = "/herds/edit")
public class HerdUpdateServlet extends HttpServlet {

    // ATRIBUTO
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
    }

    // EXIBE FORMULÁRIO DE EDIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                UUID id = UUID.fromString(idParam);
                Herd herd = herdService.findById(id);

                if (herd != null) {
                    request.setAttribute("herd", herd);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/herds/herds.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar rebanho para edição", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/herds");
    }

    // PROCESSA A ATUALIZAÇÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
            UUID idHerd =
                    UUID.fromString(request.getParameter("idHerd"));

            String name =
                    request.getParameter("name");

            String breed =
                    request.getParameter("breed");

            String purpose =
                    request.getParameter("purpose");

            String headCountParam =
                    request.getParameter("headCount");

            Integer headCount = null;

            if (headCountParam != null &&
                            !headCountParam.isBlank()) {
                headCount =
                        Integer.valueOf(headCountParam);
            }

            String propertyIdParam =
                    request.getParameter("propertyId");

            UUID propertyId = null;

            if (propertyIdParam != null &&
                            !propertyIdParam.isBlank()) {
                propertyId =
                        UUID.fromString(propertyIdParam);
            }

            // ATUALIZA REBANHO
            Herd herdToUpdate = new Herd(
                    idHerd,
                    propertyId,
                    headCount,
                    purpose,
                    name,
                    breed
            );

            herdService.update(herdToUpdate);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/herds");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/herds/herds.jsp").forward(request, response);
        }
    }
}