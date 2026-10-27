package br.com.argos.controller.lotServlet;

import br.com.argos.service.LotService;
import br.com.argos.model.Lot;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LotSelectServlet", value = "/lots")
public class LotSelectServlet extends HttpServlet {

    // ATRIBUTO
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException{
        try {
            // LISTAR
            List<Lot> lots = lotService.findAll();
            request.setAttribute("lots", lots);

            request.getRequestDispatcher(
                    "/WEB-INF/views/lots/lots.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar lote", e);
        }
    }
}