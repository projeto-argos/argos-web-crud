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
import java.util.List;

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
            // LISTA LOTES E REBANHOS
            List<Lot> lots = lotService.findAll();
            List<Herd> herds = herdService.findAll();
            request.setAttribute("lots", lots);
            request.setAttribute("herds", herds);

            request.getRequestDispatcher(
                    "/WEB-INF/views/lots/lots.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // TRATA ERRO
            throw new ServletException("Erro ao listar lotes", e);
        }
    }
}
