package br.com.argos.controller.lotServlet;

import br.com.argos.exceptions.ArgosException;
import br.com.argos.model.Lot;
import br.com.argos.model.Herd;
import br.com.argos.service.LotService;
import br.com.argos.service.HerdService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "LotDeleteServlet", value = "/lots/delete")
public class LotDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private LotService lotService;
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
        herdService = new HerdService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");
        String herdIdParam = request.getParameter("herdId");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // EXCLUI LOTE
                UUID id = UUID.fromString(idParam);
                lotService.delete(id);

                response.sendRedirect(request.getContextPath() + "/lots");
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir lote", e);
            }
        }

        if (herdIdParam != null && !herdIdParam.isBlank()) {
            try {
                // EXCLUI OS LOTES DO REBANHO
                lotService.deleteByHerd(UUID.fromString(herdIdParam));

            } catch (ArgosException e) {
                // TRATA ERRO
                request.setAttribute("erro", e.getMessage());
                List<Lot> lots = lotService.findAll();
                List<Herd> herds = herdService.findAll();
                request.setAttribute("lots", lots);
                request.setAttribute("herds", herds);
                request.getRequestDispatcher("/WEB-INF/views/lots/lots.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir lote", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/lots");
    }
}