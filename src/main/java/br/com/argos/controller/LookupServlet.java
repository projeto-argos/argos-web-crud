package br.com.argos.controller;

import br.com.argos.dao.UserDAO;
import br.com.argos.dao.PropertyDAO;
import br.com.argos.dao.HerdDAO;
import br.com.argos.dao.LotDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "LookupServlet", value = "/lookup")
public class LookupServlet extends HttpServlet {

    // ATRIBUTOS
    private UserDAO userDAO = new UserDAO();
    private PropertyDAO propertyDAO = new PropertyDAO();
    private HerdDAO herdDAO = new HerdDAO();
    private LotDAO lotDAO = new LotDAO();

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        dao = new LookupDAO();
    }

    // PROCESSA BUSCA
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        try {
            // RECEBE DADOS DA BUSCA
            String type =
                    request.getParameter("type");

            String lookup =
                    request.getParameter("lookup");


            // VERIFICA TEXTO DA BUSCA
            if (lookup == null) {
                lookup = "";
            }

            lookup = lookup.trim();

            if (lookup.isEmpty()) {
                response.getWriter().write("[]");
                return;
            }


            // REALIZA BUSCA
            List<Map<String, String>> results;

            if ("user".equals(type)) {

                results = UserDAO.lookup(lookup);

            } else if ("property".equals(type)) {

                results = propertieDAO.lookup(lookup);

            } else if ("herd".equals(type)) {

                results = herdDAO.lookup(lookup);

            } else if ("lot".equals(type)) {

                results = lotDAO.lookup(lookup);

            }

            // DEVOLVE RESULTADOS EM JSON
            response.getWriter().write(
                    createJson(results)
            );

        } catch (RuntimeException e) {

            // TRATA ERRO
            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write(
                    "{\"error\":\"Erro ao realizar busca\"}"
            );

            getServletContext().log(
                    "Erro ao realizar busca",
                    e
            );
        }
    }

    // CONVERTE RESULTADOS PARA JSON
    private String createJson(List<Map<String, String>> results) {

        StringBuilder json = new StringBuilder("[");

        boolean first = true;

        for (Map<String, String> result : results) {

            if (!first) {
                json.append(',');
            }

            first = false;

            json.append('{');

            boolean firstField = true;

            for (Map.Entry<String, String> content : result.entrySet()) {

                if (!firstField) {
                    json.append(',');
                }

                firstField = false;

                json.append('"')
                        .append(content.getKey())
                        .append("\":");

                if (content.getValue() == null) {

                    json.append("null");

                } else {

                    json.append('"')
                            .append(content.getValue())
                            .append('"');
                }
            }
            json.append('}');
        }
        return json.append(']').toString();
    }
}