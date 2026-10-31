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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // RECEBE FILTROS
            String earTag =
                    request.getParameter("earTag");

            String lotId =
                    request.getParameter("lotId");

            String exceptionReason =
                    request.getParameter("exceptionReason");

            // GUARDA OS FILTROS PARA A TELA
            Map<String, String> filter = new HashMap<>();
            filter.put("earTag", earTag);
            filter.put("lotId", lotId);
            filter.put("exceptionReason", exceptionReason);
            request.setAttribute("filter", filter);

            // LISTA ANIMAIS E LOTES
            List<Animal> animals = animalService.filter(null, earTag, null, exceptionReason, null, null, null, null);
            List<Lot> lots = lotService.findAll();

            // FILTRA POR LOTE
            if (lotId != null && !lotId.isBlank()) {
                List<Animal> animalsByLot = new ArrayList<>();

                for (int i = 0; i < animals.size(); i++) {
                    Animal animal = animals.get(i);

                    if (lotId.equals(String.valueOf(animal.getLotId()))) {
                        animalsByLot.add(animal);
                    }
                }
                animals = animalsByLot;
            }
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