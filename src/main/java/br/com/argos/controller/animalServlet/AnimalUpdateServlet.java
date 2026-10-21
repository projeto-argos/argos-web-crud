package br.com.argos.controller.animalServlet;

import br.com.argos.service.AnimalService;
import br.com.argos.model.Animal;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@WebServlet(name = "AnimalUpdateServlet", value = "/animals/edit")
public class AnimalUpdateServlet extends HttpServlet {

    // ATRIBUTO
    private AnimalService animalService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
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
                Animal animal = animalService.findById(id);

                if (animal != null) {
                    request.setAttribute("animal", animal);
                    request.getRequestDispatcher(
                            "/WEB-INF/views/animals/users.jsp"
                    ).forward(request, response);
                    return;
                }
            } catch (Exception e) {
                throw new ServletException("Erro ao buscar animal para edição", e);
            }
        }

        response.sendRedirect(request.getContextPath() + "/animals");
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
            UUID idAnimal =
                    UUID.fromString(request.getParameter("idAnimal"));

            String earTag =
                    request.getParameter("earTag");

            String weightStr =
                    request.getParameter("weight");

            BigDecimal weight = null;

            if (
                    weightStr != null &&
                            !weightStr.isBlank()
            ) {
                weight = new BigDecimal(weightStr);
            }

            String birthDateStr =
                    request.getParameter("birthDate");

            LocalDate birthDate = null;

            if (
                    birthDateStr != null &&
                            !birthDateStr.isBlank()
            ) {
                birthDate = LocalDate.parse(birthDateStr);
            }


            String exceptionReason =
                    request.getParameter("exceptionReason");

            String exceptionStartDateStr =
                    request.getParameter("exceptionStartDate");

            LocalDate exceptionStartDate = null;

            if (
                    exceptionStartDateStr != null &&
                            !exceptionStartDateStr.isBlank()
            ) {
                exceptionStartDate = LocalDate.parse(exceptionStartDateStr);
            }

            String exceptionEndDateStr =
                    request.getParameter("exceptionEndDate");

            LocalDate exceptionEndDate = null;

            if (
                    exceptionEndDateStr != null &&
                            !exceptionEndDateStr.isBlank()
            ) {
                exceptionEndDate = LocalDate.parse(exceptionEndDateStr);
            }

            String clearedForSlaughterParam =
                    request.getParameter("clearedForSlaughter");

            boolean clearedForSlaughter = request.getParameter("clearedForSlaughter") != null &&
                    request.getParameter("clearedForSlaughter").equals("true");


            String notes =
                    request.getParameter("notes");

            String batchIdStr =
                    request.getParameter("batchId");

            UUID batchId = null;

            if (
                    batchIdStr != null &&
                            !batchIdStr.isBlank()
            ) {
                batchId = UUID.fromString(batchIdStr);
            }

            String originBatchIdStr =
                    request.getParameter("originBatchId");

            UUID originBatchId = null;

            if (
                    originBatchIdStr != null &&
                            !originBatchIdStr.isBlank()
            ) {
                originBatchId = UUID.fromString(originBatchIdStr);
            }

            boolean active = request.getParameter("active") != null &&
                    request.getParameter("active").equals("true");

            // ATUALIZA ANIMAL
            Animal animalToUpdate = new Animal(
                    idAnimal,
                    batchId,
                    originBatchId,
                    weight,
                    earTag,
                    notes,
                    exceptionReason,
                    exceptionStartDate,
                    exceptionEndDate,
                    birthDate,
                    null,
                    active,
                    clearedForSlaughter
            );

            animalService.update(animalToUpdate);

            // REDIRECIONA
            response.sendRedirect(request.getContextPath() + "/animals");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/animals/users.jsp").forward(request, response);
        }
    }
}