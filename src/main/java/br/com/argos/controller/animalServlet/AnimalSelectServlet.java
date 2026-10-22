package br.com.argos.controller.animalServlet;

import br.com.argos.service.AnimalService;
import br.com.argos.model.Animal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AnimalSelectServlet", value = "/animals")
public class AnimalSelectServlet extends HttpServlet {

    // ATRIBUTO
    private AnimalService animalService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
    }

    // REQUISIÇÃO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException{

        try {
            // LISTAR
            List<Animal> animals = animalService.findAll();

            request.setAttribute("animals", animals);

            // ENCAMINHA PARA JSP
            request.getRequestDispatcher(
                    "/WEB-INF/views/animals/animals.jsp"
            ).forward(request, response);

        } catch (Exception e) {
            // ERRO
            throw new ServletException("Erro ao listar animais", e);
        }
    }
}