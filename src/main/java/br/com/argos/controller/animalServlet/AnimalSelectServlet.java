package br.com.argos.controller.animalServlet;

import br.com.argos.service.AnimalService;
import br.com.argos.service.LotService;
import br.com.argos.model.Animal;
import br.com.argos.model.Lot;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnimalSelectServlet", value = "/animals")
public class AnimalSelectServlet extends HttpServlet {

    // ATRIBUTOS
    private AnimalService animalService;
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
        lotService = new LotService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        try {
            // LISTA ANIMAIS E LOTES
            List<Animal> animals = animalService.findAll();
            List<Lot> lots = lotService.findAll();
            request.setAttribute("animals", animals);
            request.setAttribute("lots", lots);

            request.getRequestDispatcher(
                    "/WEB-INF/views/animals/animals.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar animais", e);
        }
    }
}