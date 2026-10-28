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
import java.util.List;
import java.util.UUID;

@WebServlet(name = "LotUpdateServlet", value = "/lots/edit")
public class LotUpdateServlet extends HttpServlet {

    // ATRIBUTOS
    private LotService lotService;
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
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
                Lot lot = lotService.findById(id);

                if (lot != null) {
                    List<Herd> herds = herdService.findAll();

                    request.setAttribute("herds", herds);
                    request.setAttribute("lot", lot);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/lots/lots.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar lote para edição", e);
            }
        }
        response.sendRedirect(request.getContextPath() + "/lots");
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
            UUID idLot = UUID.fromString(
                    request.getParameter("idLot")
            );

            UUID herdId = UUID.fromString(
                    request.getParameter("herdId")
            );

            String headCountString =
                    request.getParameter("headCount");

            int headCount =
                    Integer.parseInt(headCountString);

            String category =
                    request.getParameter("category");

            String lotCode =
                    request.getParameter("lotCode");


            String openDateString =
                    request.getParameter("openDate");

            LocalDate openDate = null;

            if (openDateString != null &&
                    !openDateString.isBlank()) {
                openDate = LocalDate.parse(openDateString);
            }

            // ATUALIZA LOTE
            Lot lotToUpdate = new Lot(
                    idLot,
                    herdId,
                    headCount,
                    category,
                    lotCode,
                    openDate
            );

            lotService.update(lotToUpdate);

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
