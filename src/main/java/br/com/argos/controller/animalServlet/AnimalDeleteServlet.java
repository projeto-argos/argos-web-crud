package br.com.argos.controller.animalServlet;

import br.com.argos.exceptions.ArgosException;
import br.com.argos.model.Animal;
import br.com.argos.model.Lot;
import br.com.argos.service.AnimalService;
import br.com.argos.service.LotService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AnimalDeleteServlet", value = "/animals/delete")
public class AnimalDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private AnimalService animalService;
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
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
        String earTagParam = request.getParameter("earTag");

        if (idParam != null && !idParam.isBlank()) {
            try {
                // EXCLUI ANIMAL
                UUID id = UUID.fromString(idParam);
                animalService.delete(id);

                response.sendRedirect(request.getContextPath() + "/animals");
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir animal", e);
            }
        }

        if (earTagParam != null && !earTagParam.isBlank()) {
            try {
                // EXCLUI ANIMAL PELO BRINCO
                animalService.deleteByEarTag(earTagParam);

            } catch (ArgosException e) {
                // TRATA ERRO
                request.setAttribute("erro", e.getMessage());
                List<Animal> animals = animalService.findAll();
                List<Lot> lots = lotService.findAll();
                request.setAttribute("animals", animals);
                request.setAttribute("lots", lots);
                request.getRequestDispatcher("/WEB-INF/views/animals/animals.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir animal", e);
            }
        }
        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/animals");
    }
}