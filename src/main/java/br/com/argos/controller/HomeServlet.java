package br.com.argos.controller;

import br.com.argos.service.HomeService;
import br.com.argos.model.HomeSummary;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends HttpServlet {

    // ATRIBUTOS
    private HomeService homeService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        homeService = new HomeService();
    }

    // EXIBE PÁGINA INICIAL
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            HomeSummary summary = homeService.getSummary(10);
            request.setAttribute("summary", summary);

            request.getRequestDispatcher(
                    "/WEB-INF/views/home/home.jsp"
            ).forward(request, response);
        } catch (Exception e) {
            // TRATA ERRO
            throw new ServletException("Erro ao carregar a página inicial.", e);
        }
    }
}
