package br.com.argos.controller.propertyServlet;

import br.com.argos.service.PropertyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "PropertyDeleteServlet", value = "/properties/delete")
public class PropertyDeleteServlet extends HttpServlet {

    // ATRIBUTO
    private PropertyService propertyService;

    // CICLO DE VIDA DO SERVLET
    @Override
    public void init() {
        propertyService = new PropertyService();
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

                // EXCLUI PROPRIEDADE
                UUID id = UUID.fromString(idParam);
                propertyService.delete(id);

            } catch (Exception e) {
                // TRATA ERRO
                throw new ServletException("Erro ao excluir propriedade", e);
            }
        }

        // REDIRECIONA
        response.sendRedirect(request.getContextPath() + "/properties");
    }
}