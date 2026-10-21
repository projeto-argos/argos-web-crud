package br.com.argos.controller.herdServlet;

import br.com.argos.service.HerdService;
import br.com.argos.model.Herd;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "HerdSelectServlet", value = "/herds")
public class HerdSelectServlet extends HttpServlet {

    // ATRIBUTO
    private HerdService herdService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        herdService = new HerdService();
    }

    // REQUISIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            // LISTAR
            List<Herd> herds = herdService.findAll();

            request.setAttribute("herds", herds);

            // ENCAMINHA PARA JSP
            request.getRequestDispatcher(
                    "/WEB-INF/views/herds/list.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar rebanhos", e);
        }
    }
}