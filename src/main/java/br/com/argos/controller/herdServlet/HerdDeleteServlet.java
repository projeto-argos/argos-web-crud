package br.com.argos.controller.herdServlet;

import br.com.argos.service.HerdService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "HerdDeleteServlet", value = "/herds/delete")
public class HerdDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
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

        if (idParam != null && !idParam.isBlank()) {
            try {
                // EXCLUI REBANHO
                UUID id = UUID.fromString(idParam);
                herdService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir rebanho", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/herds");
    }
}