package br.com.argos.controller.animalServlet;

import br.com.argos.service.AnimalService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "AnimalDeleteServlet", value = "/animals/delete")
public class AnimalDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private AnimalService animalService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
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

                // EXCLUI ANIMAL
                UUID id = UUID.fromString(idParam);
                animalService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir animal", e);
            }
        }

        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/animals");
    }
}