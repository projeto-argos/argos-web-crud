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

@WebServlet(name = "HerdInsertServlet", value = "/herds/register")
public class HerdInsertServlet extends HttpServlet {

    // ATRIBUTO
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
    }

    // EXIBE FORMULÁRIO DE CADASTRO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/herds/form.jsp"
        ).forward(request, response);
    }

    // PROCESSA FORMULÁRIO DE CADASTRO
    @Override
    protected void doPost(
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

            if (
                    headCountString != null &&
                            !headCountString.isBlank()
            ) {
                headCount =
                        Integer.valueOf(headCountString);
            }

            String propertyIdString =
                    request.getParameter("propertyId");

            UUID propertyId = null;

            if (
                    propertyIdString != null &&
                            !propertyIdString.isBlank()
            ) {
                propertyId =
                        UUID.fromString(propertyIdString);
            }

            boolean active =
                    request.getParameter("active") != null &&
                            request.getParameter("active").equals("true");

            // CADASTRA REBANHO
            Herd newHerd = new Herd(
                    propertyId,
                    headCount,
                    purpose,
                    name,
                    breed,
                    active
            );

            herdService.create(newHerd);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/herds");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/herds/form.jsp").forward(request, response);
        }
    }
}