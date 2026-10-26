package br.com.argos.controller;

import br.com.argos.service.UserService;
import br.com.argos.service.PropertyService;
import br.com.argos.service.HerdService;
// import br.com.argos.service.LotService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LookupServlet", value = "/lookup")
public class LookupServlet extends HttpServlet {

    // ATRIBUTOS
    private UserService userService;
    private PropertyService propertyService;
    private HerdService herdService;
//    private LotService lotService;

    private ObjectMapper objectMapper;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() throws ServletException {
        userService = new UserService();
        propertyService = new PropertyService();
        herdService = new HerdService();
//        lotService = new LotService();

        objectMapper = new ObjectMapper();
    }

    // PROCESSA A BUSCA
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // RECEBE OS DADOS
        String type =
                request.getParameter("type");

        String search =
                request.getParameter("search");


        // EXECUTA A BUSCA DO TIPO SOLICITADO

        Object results = null;
/*
        if (type.equals("user")) {
            results = userService.findUsers(search);

        } else if (type.equals("property")) {
            results = propertyService.findProperties(search);

        } else if (type.equals("herd")) {
            results = herdService.findHerds(search);

        } else if (type.equals("lot")) {
            results = lotService.findLots(search);

        }*/

        // CONVERTE OS RESULTADOS PARA JSON
        String json = objectMapper.writeValueAsString(results);
        response.getWriter().write(json);
    }
}