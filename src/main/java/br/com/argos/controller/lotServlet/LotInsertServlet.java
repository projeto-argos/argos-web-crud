package br.com.argos.controller.lotServlet;

import br.com.argos.model.Lot;
import br.com.argos.service.LotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@WebServlet(name = "LotInsertServlet", value = "/lots/register")
public class LotInsertServlet extends HttpServlet {

    // ATRIBUTO
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/lots/lots.jsp"
        ).forward(request, response);
    }

    // PROCESSA FORMULÁRI
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            String herdIdString =
                    request.getParameter("herdId");

            UUID herdId = UUID.fromString(herdIdString);

            String headCountString =
                    request.getParameter("headCount");

            Integer headCount = null;

            if (headCountString != null
                    && !headCountString.isBlank()) {
                headCount = Integer.valueOf(headCountString);
            }

            String category =
                    request.getParameter("category");

            String lotCode =
                    request.getParameter("lotCode");

            String openDateString =
                    request.getParameter("openDate");

            LocalDate openDate = null;

            if (openDateString != null
                    && !openDateString.isBlank()) {
                openDate = LocalDate.parse(openDateString);
            }

            // CADASTRA LOTE
            Lot newLot = new Lot(
                    herdId,
                    headCount,
                    category,
                    lotCode,
                    openDate
            );

            lotService.create(newLot);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/lots");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/lots/lots.jsp").forward(request, response);
        }
    }
}
