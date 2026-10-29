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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "AnimalInsertServlet", value = "/animals/register")
public class AnimalInsertServlet extends HttpServlet {

    // ATRIBUTOS
    private AnimalService animalService;
    private LotService lotService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        animalService = new AnimalService();
        lotService = new LotService();
    }

    // EXIBE FORMULÁRIO
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // BUSCA TODOS OS LOTES
        List<Lot> lots = lotService.findAll();
        request.setAttribute("lots", lots);

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

            if (weightString != null &&
                    !weightString.isBlank()) {
                weight = new BigDecimal(weightString);
            }

            String birthDateString =
                    request.getParameter("birthDate");

            LocalDate birthDate = null;

            if (birthDateString != null &&
                    !birthDateString.isBlank()) {
                birthDate = LocalDate.parse(birthDateString);
            }

            String exceptionReason =
                    request.getParameter("exceptionReason");

            String exceptionStartDateString =
                    request.getParameter("exceptionStartDate");

            LocalDate exceptionStartDate = null;

            if (exceptionStartDateString != null &&
                    !exceptionStartDateString.isBlank()) {
                exceptionStartDate =
                        LocalDate.parse(exceptionStartDateString);
            }

            String exceptionEndDateString =
                    request.getParameter("exceptionEndDate");

            LocalDate exceptionEndDate = null;

            if (exceptionEndDateString != null &&
                    !exceptionEndDateString.isBlank()) {
                exceptionEndDate =
                        LocalDate.parse(exceptionEndDateString);
            }

            String clearedForSlaughterString =
                    request.getParameter("clearedForSlaughter");

            boolean clearedForSlaughter =
                    clearedForSlaughterString != null &&
                            clearedForSlaughterString.equals("true");

            String notes =
                    request.getParameter("notes");

            String lotIdString =
                    request.getParameter("lotId");

            UUID lotId = null;

            if (lotIdString != null &&
                    !lotIdString.isBlank()) {
                lotId = UUID.fromString(lotIdString);
            }

            String originLotIdString =
                    request.getParameter("originLotId");

            UUID originLotId = null;

            if (originLotIdString != null &&
                    !originLotIdString.isBlank()) {
                originLotId = UUID.fromString(originLotIdString);
            }

            // CADASTRA ANIMAL
            Animal newAnimal = new Animal(
                    lotId,
                    originLotId,
                    weight,
                    earTag,
                    notes,
                    exceptionReason,
                    exceptionStartDate,
                    exceptionEndDate,
                    birthDate,
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
