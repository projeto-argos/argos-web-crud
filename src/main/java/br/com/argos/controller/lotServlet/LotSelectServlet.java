package br.com.argos.controller.lotServlet;

import br.com.argos.model.Herd;
import br.com.argos.model.Lot;
import br.com.argos.service.HerdService;
import br.com.argos.service.LotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LotSelectServlet", value = "/lots")
public class LotSelectServlet extends HttpServlet {

    // ATRIBUTOS
    private LotService lotService;
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
        herdService = new HerdService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            // RECEBE FILTROS
            String lotCode =
                    request.getParameter("lotCode");

            String herdId =
                    request.getParameter("herdId");

            String category =
                    request.getParameter("category");

            String headCount =
                    request.getParameter("headCount");

            String openDate =
                    request.getParameter("openDate");

            // GUARDA OS FILTROS PARA A TELA
            Map<String, String> filter = new HashMap<>();
            filter.put("lotCode", lotCode);
            filter.put("herdId", herdId);
            filter.put("category", category);
            filter.put("headCount", headCount);
            filter.put("openDate", openDate);
            request.setAttribute("filter", filter);

            LocalDate openDateFilter = null;

            if (openDate != null &&
                    !openDate.isBlank()) {
                openDateFilter = LocalDate.parse(openDate);
            }

            // LISTA LOTES E REBANHOS
            List<Lot> lots = lotService.filter(headCount, category, lotCode, openDateFilter);
            List<Herd> herds = herdService.findAll();

            // FILTRA POR REBANHO
            if (herdId != null && !herdId.isBlank()) {
                List<Lot> lotsByHerd = new ArrayList<>();

                for (int i = 0; i < lots.size(); i++) {
                    Lot lot = lots.get(i);

                    if (herdId.equals(String.valueOf(lot.getHerdId()))) {
                        lotsByHerd.add(lot);
                    }
                }
                lots = lotsByHerd;
            }
            request.setAttribute("lots", lots);
            request.setAttribute("herds", herds);

            request.getRequestDispatcher(
                    "/WEB-INF/views/lots/lots.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar lotes", e);
        }
    }
}