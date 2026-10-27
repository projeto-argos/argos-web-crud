package br.com.argos.controller.lotServlet;

import br.com.argos.service.LotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "LotDeleteServlet", value = "/lots/delete")
public class LotDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        lotService = new LotService();
    }

    // PROCESSA A EXCLUSÃO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // EXCLUI USUÁRIO
                UUID id = UUID.fromString(idParam);
                lotService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir lote", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/lots");
    }
}