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

@WebServlet(name = "AnimalInsertServlet", value = "/animals/register")
public class AnimalInsertServlet extends HttpServlet {

    // ATRIBUTO
    private AnimalService animalService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.getRequestDispatcher(
                "/WEB-INF/views/animals/animals.jsp"
        ).forward(request, response);
    }

    // PROCESSA FORMULÁRIO
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // RECEBE DADOS
            String earTag =
                    request.getParameter("earTag");

            String weightString =
                    request.getParameter("weight");

            BigDecimal weight = null;

            if (
                    weightString != null &&
                            !weightString.isBlank()
            ) {
                weight = new BigDecimal(weightString);
            }

            String birthDateString =
                    request.getParameter("birthDate");

            LocalDate birthDate = null;

            if (
                    birthDateString != null &&
                            !birthDateString.isBlank()
            ) {
                birthDate = LocalDate.parse(birthDateString);
            }

            String exceptionReason =
                    request.getParameter("exceptionReason");

            String exceptionStartDateString =
                    request.getParameter("exceptionStartDate");

            LocalDate exceptionStartDate = null;

            if (
                    exceptionStartDateString != null &&
                            !exceptionStartDateString.isBlank()
            ) {
                exceptionStartDate = LocalDate.parse(exceptionStartDateString);
            }


            String exceptionEndDateString =
                    request.getParameter("exceptionEndDate");

            LocalDate exceptionEndDate = null;

            if (
                    exceptionEndDateString != null &&
                            !exceptionEndDateString.isBlank()
            ) {
                exceptionEndDate = LocalDate.parse(exceptionEndDateString);
            }

            boolean clearedForSlaughter =
                    request.getParameter("clearedForSlaughter") != null &&
                    request.getParameter("clearedForSlaughter").equals("true");

            String notes =
                    request.getParameter("notes");

            String batchIdString =
                    request.getParameter("batchId");

            UUID batchId = null;

            if (
                    batchIdString != null &&
                            !batchIdString.isBlank()
            ) {
                batchId = UUID.fromString(batchIdString);
            }


            String originBatchIdString =
                    request.getParameter("originBatchId");

            UUID originBatchId = null;

            if (
                    originBatchIdString != null &&
                            !originBatchIdString.isBlank()
            ) {
                originBatchId = UUID.fromString(originBatchIdString);
            }

            boolean active =
                    request.getParameter("active") != null &&
                            request.getParameter("active").equals("true");

            // CADASTRA ANIMAL
            Animal newAnimal = new Animal(
                    batchId,
                    originBatchId,
                    weight,
                    earTag,
                    notes,
                    exceptionReason,
                    exceptionStartDate,
                    exceptionEndDate,
                    birthDate,
                    active,
                    clearedForSlaughter
            );

            animalService.create(newAnimal);

            // REDIRECIONA
            response.sendRedirect(
                    request.getContextPath() + "/animals");

        } catch (Exception e) {
            // TRATA ERRO
            request.setAttribute("erro", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/animals/animals.jsp").forward(request, response);
        }
    }
}